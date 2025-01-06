package movlit.be.member.infra.persistence.jpa;

import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.MemberGenre;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.entity.MemberGenreIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGenreJpaRepository extends JpaRepository<MemberGenreEntity, MemberGenreIdEntity> {

    List<Long> findAllByMemberId(MemberId memberId);

    @Query("SELECT mg.memberGenreIdEntity.genreId FROM MemberGenreEntity mg WHERE mg.memberGenreIdEntity.memberId = :memberId")
    List<Long> findGenreIdsByMemberId(@Param("memberId")MemberId memberId);

    List<MemberGenreEntity> findByMemberGenreIdEntity_MemberId(MemberId memberId);
}
