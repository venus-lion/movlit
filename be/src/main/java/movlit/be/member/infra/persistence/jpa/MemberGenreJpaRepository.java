package movlit.be.member.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.entity.MemberGenreIdEntity;
import movlit.be.member.presentation.dto.response.GenreListReadResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberGenreJpaRepository extends JpaRepository<MemberGenreEntity, MemberGenreIdEntity> {

    @Query("SELECT mg.memberGenreIdEntity.genreId FROM MemberGenreEntity mg WHERE mg.memberGenreIdEntity.memberId = :memberId")
    List<Long> findGenreIdsByMemberId(@Param("memberId")MemberId memberId);

    @Query("SELECT mg "
            + "FROM MemberGenreEntity mg "
            + "WHERE mg.memberGenreIdEntity.memberId = :memberId")
    Optional<List<MemberGenreEntity>> findAllByMemberId(MemberId memberId);

}
