package movlit.be;

import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository MemberRepository;

    @Override
    public Member findByMemberId(String memberId) {
        return MemberRepository.findById(memberId).orElse(null);
    }

    @Override
    public List<Member> getMembers() {
        return MemberRepository.findAll();
    }

    @Override
    public void registerMember(Member member) {
        MemberRepository.save(member);
    }

    @Override
    public void updateMember(Member member) {
        MemberRepository.save(member);
    }

    @Override
    public void deleteMember(String memberId) {
        MemberRepository.deleteById(memberId);
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
