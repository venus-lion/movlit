package movlit.be.member.infra.persistence.jpa;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.entity.MemberGenreIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGenreJpaRepository extends JpaRepository<MemberGenreEntity, MemberGenreIdEntity> {

    @Query("SELECT mg.memberGenreIdEntity.genreId FROM MemberGenreEntity mg WHERE mg.memberGenreIdEntity.memberId = :memberId")
    List<Long> findGenresByMemberId(@Param("memberId")MemberId memberId);
}
