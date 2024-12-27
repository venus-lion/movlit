package movlit.be.member.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, MemberId> {

    //@Query("SELECT m FROM MemberEntity m WHERE m.email = :email")
    Optional<MemberEntity> findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

}
