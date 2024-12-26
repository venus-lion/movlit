package movlit.be.member.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.repository.MemberRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;

    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int Member_NOT_EXIST = 2;

    public Member findByMemberId(String memberId) {

        return memberRepository.findById(memberId);
    }

    public int login(String memberId, String pwd) {
        Member member = findByMemberId(memberId);
        if (member == null) {
            return Member_NOT_EXIST;
        }
        if (BCrypt.checkpw(pwd, member.getPassword())) {
            return CORRECT_LOGIN;
        }
        return WRONG_PASSWORD;
    }

}
