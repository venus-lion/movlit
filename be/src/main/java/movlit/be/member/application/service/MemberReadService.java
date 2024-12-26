package movlit.be.member.application.service;

import java.util.List;
import movlit.be.MemberRepository;
import movlit.be.MemberService;
import movlit.be.member.domain.Member;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberReadService implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member findByMemberId(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public List<Member> getMembers() {
        return memberRepository.findAll();
    }


    @Override
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