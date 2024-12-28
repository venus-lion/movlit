package movlit.be.member.presentation;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.dto.AuthTokenIssueResponse;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.Member;
import movlit.be.member.presentation.dto.request.MemberLoginRequest;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@RequestBody MemberRegisterRequest request) {
        var response = memberWriteService.registerMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenIssueResponse> login(@RequestBody MemberLoginRequest request) {
        var response = memberWriteService.login(request);
        return ResponseEntity.ok().body(response);
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
