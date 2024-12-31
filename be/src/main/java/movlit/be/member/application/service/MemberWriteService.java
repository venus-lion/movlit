package movlit.be.member.application.service;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.DuplicateEmailException;
import movlit.be.common.exception.DuplicateNicknameException;
import movlit.be.common.exception.MemberPasswordMismatchException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.presentation.dto.request.MemberLoginRequest;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import movlit.be.new_auth.application.dto.response.TokenIssueResponse;
import movlit.be.new_auth.application.service.TokenService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberReadService memberReadService;
    private final TokenService tokenService;

    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        String dob = request.getDob();
        String email = request.getEmail();
        String nickname = request.getNickname();
        String password = request.getPassword();

        String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
        Member member = Member.builder()
                .memberId(IdFactory.createMemberId())
                .password(hashedPwd)
                .nickname(nickname)
                .email(email)
                .dob(dob)
                .regDt(LocalDateTime.now())
                .role("ROLE_Member")
                .provider("local")
                .build();

        validateNicknameDuplication(member.getNickname());
        validateEmailDuplication(member.getEmail());

        Member savedMember = memberRepository.save(member);
        return MemberRegisterResponse.from(savedMember.getMemberId());
    }

    private void validateNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateEmailDuplication(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    public void deleteMember(MemberId memberId) {
        memberRepository.deleteById(memberId);
    }

    public TokenIssueResponse login(MemberLoginRequest request) {
        // TODO: usecase를 안 쓰니 write 서비스에서 read 서비스를 참조하는 일이 생김
        Member member = memberReadService.findByMemberEmail(request.getEmail());
        checkPasswordMatch(request, member);
        return tokenService.issue(new Date(), member);
    }

    private void checkPasswordMatch(MemberLoginRequest request, Member member) {
        if (!BCrypt.checkpw(request.getPassword(), member.getPassword())) {
            throw new MemberPasswordMismatchException();
        }
    }

    public void logout(String accessToken) {
        tokenService.revoke(accessToken);
    }

}

