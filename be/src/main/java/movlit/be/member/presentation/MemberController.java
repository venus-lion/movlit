package movlit.be.member.presentation;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.annotation.CurrentMemberId;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.Member;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @GetMapping("/register")
    public String registerForm() {
        return "member/register";
    }

    @PostMapping("/register")
    public String registerProc(@CurrentMemberId MemberId memberId, String pwd, String pwd2, String uname, String email,
                               String profileUrl) {
        if (memberId == null && pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            MemberId generatedMemberId = IdFactory.createMemberId();
            Member member = Member.builder()
                    .memberId(generatedMemberId).password(hashedPwd).nickname(uname).email(email)
                    .profileImgUrl(profileUrl)
                    .regDt(LocalDateTime.now()).role("ROLE_Member").provider("local")
                    .build();

            memberWriteService.registerMember(member);
        }
        return "redirect:/member/list";
    }

    @GetMapping("/delete/{memberId}")
    public String delete(@PathVariable MemberId memberId) {
        memberWriteService.deleteMember(memberId);
        return "redirect:/member/list";
    }

    @GetMapping("/update/{memberId}")
    public String updateForm(@PathVariable MemberId memberId, Model model) {
        Member member = memberReadService.findByMemberId(memberId);
        model.addAttribute("member", member);
        return "member/update";
    }

    @PostMapping("/update")
    public String updateProc(MemberId memberId, String pwd, String pwd2, String uname, String email, String profileUrl,
                             String role, String provider) {
        Member member = memberReadService.findByMemberId(memberId);
        if (pwd.equals(pwd2) && pwd.length() >= 4) {
            String hashedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            member.setPassword(hashedPwd);
        }
        member.setMemberId(memberId);
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
    public String loginProc(String email, String pwd, HttpSession session, Model model) {
        String msg, url;
        int result = memberReadService.login(email, pwd);
        if (result == memberReadService.CORRECT_LOGIN) {
            Member member = memberReadService.findByMemberEmail(email);
            String nickname = member.getNickname();
            session.setAttribute("sessEmail", email);
            session.setAttribute("sessNickname", nickname);
            msg = member.getNickname() + "님 환영합니다.";
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
        session.setMaxInactiveInterval(4 * 60 * 60);        // 세션 타임아웃 시간: 4시간
        String msg = "로그인이 완료되었습니다.";
        String url = "index";
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
