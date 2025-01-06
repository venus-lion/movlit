package movlit.be.movie.infra.persistence.jpa;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;
import movlit.be.movie.domain.entity.MovieHeartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovieHeartJpaRepository extends JpaRepository<MovieHeartEntity, MovieHeartId> {

    // 유저의 가장 최근의 찜을 한 영화
//    @Query("SELECT m FROM MovieHeartEntity m where m.memberId = :memberId ORDER BY m.regDt DESC")
    Optional<MovieHeartEntity> findTopByMemberIdOrderByRegDtDesc(MemberId memberId);
}
