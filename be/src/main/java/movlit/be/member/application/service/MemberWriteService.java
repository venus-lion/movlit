package movlit.be.member.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.DuplicateEmailException;
import movlit.be.common.exception.DuplicateNicknameException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberWriteService {

    private final MemberRepository memberRepository;

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


}

