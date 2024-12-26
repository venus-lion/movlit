package movlit.be.member.presentation;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.Member;
import org.mindrot.jbcrypt.BCrypt;
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
@RequiredArgsConstructor
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @GetMapping("/register")
    public String registerForm() {
        return "member/register";
    }

    @PostMapping("/register")
    public String registerProc(String memberId, String pwd, String pwd2, String uname, String email,
                               String profileUrl) {
        if (memberReadService.findByMemberId(memberId) == null && pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            Member member = Member.builder()
                    .memberId(memberId).password(hashedPwd).nickname(uname).email(email).profileImgUrl(profileUrl)
                    .regDt(LocalDateTime.now()).role("ROLE_Member").provider("local")
                    .build();

            memberWriteService.registerMember(member);
        }
        return "redirect:/member/list";
    }

    @GetMapping("/delete/{memberId}")
    public String delete(@PathVariable String memberId) {
        memberWriteService.deleteMember(memberId);
        return "redirect:/member/list";
    }

    @GetMapping("/update/{memberId}")
    public String updateForm(@PathVariable String memberId, Model model) {
        Member member = memberReadService.findByMemberId(memberId);
        model.addAttribute("member", member);
        return "member/update";
    }

    @PostMapping("/update")
    public String updateProc(String memberId, String pwd, String pwd2, String uname, String email, String profileUrl,
                             String role, String provider) {
        Member member = memberReadService.findByMemberId(memberId);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            member.setPassword(hashedPwd);
        }
        member.setMemberId(uname);
        member.setEmail(email);
        member.setProfileImgUrl(profileUrl);
        member.setRole(role);
        memberWriteService.updateMember(member);
        return "redirect:/member/list";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @PostMapping("/login")
    public String loginProc(String memberId, String pwd, HttpSession session, Model model) {
        String msg, url;
        int result = memberReadService.login(memberId, pwd);
        if (result == memberReadService.CORRECT_LOGIN) {
            Member member = memberReadService.findByMemberId(memberId);
            session.setAttribute("sessmemberId", memberId);
            session.setAttribute("sessUname", member.getMemberId());
            msg = member.getMemberId() + "님 환영합니다.";
            url = "/member/list";
        } else if (result == memberReadService.WRONG_PASSWORD) {
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
        String memberId = authentication.getName();

        Member member = memberReadService.findByMemberId(memberId);
        session.setAttribute("sessmemberId", memberId);
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
