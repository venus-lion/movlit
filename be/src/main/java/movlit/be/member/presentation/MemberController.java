package movlit.be.member.presentation;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.auth.application.service.dto.AuthTokenIssueResponse;
import movlit.be.common.util.HttpHeaderParser;
import movlit.be.common.util.HttpHeaderType;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.application.service.MemberWriteService;
import movlit.be.member.domain.Member;
import movlit.be.member.presentation.dto.request.MemberLoginRequest;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.response.GenreListReadResponse;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @PostMapping("/api/members/register")
    public ResponseEntity<MemberRegisterResponse> register(@RequestBody @Valid MemberRegisterRequest request) {
        var response = memberWriteService.registerMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/api/members/login")
    public ResponseEntity<AuthTokenIssueResponse> login(@RequestBody MemberLoginRequest request) {
        var response = memberWriteService.login(request);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원 장르 조회 API
     */
    @GetMapping("/api/members/genreList")
    public ResponseEntity<List<GenreListReadResponse>> fetchGenreList(@AuthenticationPrincipal MyMemberDetails details) {
        var response = memberReadService.fetchGenreListById(details.getMemberId());
        return ResponseEntity.ok(response);
    }

    /**
     * 장르 조회 API (로그인 정보 X)
     */
    @GetMapping("/api/genreList")
    public ResponseEntity<List<GenreListReadResponse>> fetchGenreList() {
        var response = memberReadService.fetchGenreList();
        return ResponseEntity.ok(response);
    }

    /**
     * Authorization: Bearer <accessToken>
     */
    @GetMapping("/api/members/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String accessToken = HttpHeaderParser.parse(authHeader, HttpHeaderType.AUTH);
        memberWriteService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/members/delete/{memberId}")
    public String delete(@PathVariable MemberId memberId) {
        memberWriteService.deleteMember(memberId);
        return "redirect:/member/list";
    }

    @GetMapping("/api/members/update/{memberId}")
    public String updateForm(@PathVariable MemberId memberId, Model model) {
        Member member = memberReadService.findByMemberId(memberId);
        model.addAttribute("member", member);
        return "member/update";
    }

    @PostMapping("/api/members/update")
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

}
