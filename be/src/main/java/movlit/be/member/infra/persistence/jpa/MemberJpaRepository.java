package movlit.be.member.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, MemberId> {

    Optional<MemberEntity> findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

}
