package movlit.be.member.infra.persistence.jpa;

import java.util.List;
import movlit.be.common.util.ids.MemberGenreId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGenreJpaRepository extends JpaRepository<MemberGenreEntity, MemberGenreId> {

    @Query("SELECT mg.genreId FROM MemberGenreEntity mg WHERE mg.memberId = :memberId")
    List<Long> findGenreIdsByMemberId(@Param("memberId") MemberId memberId);

    @Query("SELECT mg "
            + "FROM MemberGenreEntity mg "
            + "WHERE mg.memberId = :memberId")
    List<MemberGenreEntity> findAllByMemberId(MemberId memberId);

}
