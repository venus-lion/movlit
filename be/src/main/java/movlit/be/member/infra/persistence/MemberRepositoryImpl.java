package movlit.be.member.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.infra.persistence.jpa.MemberJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        MemberEntity memberToEntity = MemberConverter.toEntity(member);
        MemberEntity memberEntity = memberJpaRepository.save(memberToEntity);
        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public Member findById(MemberId memberId) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public Member findByEmail(String email) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public void deleteById(MemberId memberId) {
        memberJpaRepository.deleteById(memberId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

}
