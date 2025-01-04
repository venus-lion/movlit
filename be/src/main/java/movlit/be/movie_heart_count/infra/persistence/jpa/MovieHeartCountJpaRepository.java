package movlit.be.movie_heart_count.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MovieHeartCountId;
import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MovieHeartCountJpaRepository extends JpaRepository<MovieHeartCountEntity, MovieHeartCountId> {

    @Modifying
    @Query("UPDATE MovieHeartCountEntity mhc "
            + "SET mhc.count = mhc.count + 1 "
            + "WHERE mhc.movieId = :movieId")
    void incrementMovieHeartCount(Long movieId);

    @Modifying
    @Query("UPDATE MovieHeartCountEntity mhc "
            + "SET mhc.count = mhc.count - 1 "
            + "WHERE mhc.movieId = :movieId")
    void decrementMovieHeartCount(Long movieId);

    Optional<MovieHeartCountEntity> findByMovieId(Long movieId);

}
