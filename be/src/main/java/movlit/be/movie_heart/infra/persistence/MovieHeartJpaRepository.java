package movlit.be.movie_heart.infra.persistence;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieHeartJpaRepository extends JpaRepository<MovieHeartEntity, MovieHeartId> {

    boolean existsByMovieIdAndMemberId(Long movieId, MemberId memberId);

    void deleteByMovieIdAndMemberId(Long movieId, MemberId memberId);

    @Override
    List<MovieHeartEntity> findAll();

    List<MovieHeartEntity> findTop3ByMemberIdOrderByRegDtDesc(MemberId memberId);


    @Query("SELECT mh.memberId "
            + "FROM MovieHeartEntity mh "
            + "WHERE mh.movieId = :movieId")
    Optional<List<MemberId>> findMemberByMovieId(@Param("movieId") Long movieId);

}
