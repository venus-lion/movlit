package movlit.be.member.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.repository.MemberRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReadService {

    private final MemberRepository memberRepository;

    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int Member_NOT_EXIST = 2;

    public Member findByMemberEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public int login(String email, String pwd) {
        Member member = findByMemberEmail(email);
        // TODO: login 실패 로직 짜기
        if (member == null) {
            return Member_NOT_EXIST;
        }
        if (BCrypt.checkpw(pwd, member.getPassword())) {
            return CORRECT_LOGIN;
        }
        return WRONG_PASSWORD;
    }

    public Member findByMemberId(MemberId memberId) {
        return memberRepository.findById(memberId);
    }

}
