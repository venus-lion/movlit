package movlit.be.member.infra.persistence.jpa;

import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, String> {

}
