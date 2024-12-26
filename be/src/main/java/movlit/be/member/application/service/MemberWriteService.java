package movlit.be.member.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.MemberRepository;
import movlit.be.MemberService;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWriteService implements MemberService {

    @Autowired
    private MemberRepository memberRepository;


    @Override
    public void registerMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void deleteMember(String memberId) {
        memberRepository.deleteById(memberId);
    }



}

