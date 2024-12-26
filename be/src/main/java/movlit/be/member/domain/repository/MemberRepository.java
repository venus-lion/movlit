package movlit.be.member.domain.repository;

import java.util.Optional;
import movlit.be.member.domain.entity.MemberEntity;

public interface MemberRepository {
    MemberEntity save(MemberEntity memberEntity);

    Optional<MemberEntity> findById(String id);

    void deleteById(String id);
}
