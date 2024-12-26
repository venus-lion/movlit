package movlit.be;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/register")
    public String registerForm() {
        return "member/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd, String pwd2, String uname, String email, String profileUrl) {
        if (memberService.findByMemberId(uid) == null && pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            Member member = Member.builder()
                    .memberId(uid).password(hashedPwd).nickname(uname).email(email).profileImgUrl(profileUrl)
                    .regDt(LocalDateTime.now()).role("ROLE_Member").provider("local")
                    .build();

            memberService.registerMember(member);
        }
        return "redirect:/member/list";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        List<Member> memberList = memberService.getMembers();
        session.setAttribute("menu", "member");
        model.addAttribute("memberList", memberList);
        return "member/list";
    }

    @GetMapping("/delete/{uid}")
    public String delete(@PathVariable String uid) {
        memberService.deleteMember(uid);
        return "redirect:/member/list";
    }

    @GetMapping("/update/{uid}")
    public String updateForm(@PathVariable String uid, Model model) {
        Member member = memberService.findByMemberId(uid);
        model.addAttribute("member", member);
        return "member/update";
    }

    @PostMapping("/update")
    public String updateProc(String uid, String pwd, String pwd2, String uname, String email, String profileUrl,
                             String role, String provider) {
        Member member = memberService.findByMemberId(uid);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            member.setPassword(hashedPwd);
        }
        member.setMemberId(uname);
        member.setEmail(email);
        member.setProfileImgUrl(profileUrl);
        member.setRole(role);
        //member.sets(provider);
        memberService.updateMember(member);
        return "redirect:/member/list";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @PostMapping("/login")
    public String loginProc(String uid, String pwd, HttpSession session, Model model) {
        String msg, url;
        int result = memberService.login(uid, pwd);
        if (result == memberService.CORRECT_LOGIN) {
            Member member = memberService.findByMemberId(uid);
            session.setAttribute("sessUid", uid);
            session.setAttribute("sessUname", member.getMemberId());
            msg = member.getMemberId() + "님 환영합니다.";
            url = "/mall/list";
        } else if (result == memberService.WRONG_PASSWORD) {
            msg = "패스워드가 틀렸습니다.";
            url = "/member/login";
        } else {
            msg = "입력한 아이디가 존재하지 않습니다.";
            url = "/member/register";
        }
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "common/alertMsg";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model) {
        // Spring Security 현재 세션의 사용자 아이디
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();

        Member member = memberService.findByMemberId(uid);
        session.setAttribute("sessUid", uid);
        session.setAttribute("sessUname", member.getMemberId());
        session.setMaxInactiveInterval(4 * 60 * 60);        // 세션 타임아웃 시간: 4시간
        String msg = member.getMemberId() + "님 환영합니다.";
        String url = "/mall/list";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "common/alertMsg";
    }

    @GetMapping("/loginFailure")
    public String loginFailure(Model model) {
        String msg = "잘못 입력하였습니다.";
        String url = "/Member/login";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "common/alertMsg";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/member/login";
    }

}
