package movlit.be.member.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;

    public void deleteMember(String memberId) {
        MemberRepository.deleteById(memberId);
    }

}
