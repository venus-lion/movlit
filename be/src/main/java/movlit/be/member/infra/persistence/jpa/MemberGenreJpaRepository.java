package movlit.be.member.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.entity.MemberGenreIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberGenreJpaRepository extends JpaRepository<MemberGenreEntity, MemberGenreIdEntity> {

    @Query("SELECT mg "
            + "FROM MemberGenreEntity mg "
            + "WHERE mg.memberEntity.memberId = :memberId")
    Optional<List<MemberGenreEntity>> findAllByMemberId(MemberId memberId);

}
