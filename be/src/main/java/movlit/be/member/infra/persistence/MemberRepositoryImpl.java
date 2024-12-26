package movlit.be.member.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberNotFoundException;
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
        MemberEntity memberEntity = memberJpaRepository.save(MemberConverter.toEntity(member));
        return MemberConverter.toDomain(memberEntity);
    }

    ;

    @Override
    public Member findById(String id) {
        MemberEntity memberEntity = memberJpaRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public void deleteById(String id) {
        memberJpaRepository.deleteById(id);
    }

}
