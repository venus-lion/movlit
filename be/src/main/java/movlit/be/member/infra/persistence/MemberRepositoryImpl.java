package movlit.be.member.infra.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.infra.persistence.jpa.MemberJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public MemberEntity save(MemberEntity memberEntity){
        return memberJpaRepository.save(memberEntity);
    };

    @Override
    public Optional<MemberEntity> findById(String id){
        return memberJpaRepository.findById(id);
    }

    @Override
    public void deleteById(String id){
        memberJpaRepository.deleteById(id);
    }
}
