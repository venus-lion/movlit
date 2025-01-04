package movlit.be.movie.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.presentation.dto.response.MovieDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieDetailJpaRepository extends JpaRepository<MovieEntity, Long> {

    /**
     * Long movieId, String title, String originalTitle, String overview,
     * Double popularity, Long heartCount, boolean isHearted, String posterPath,
     * String backdropPath, LocalDate releaseDate, String productionCountry,
     * String originalLanguage, Integer runtime, String status, Long voteCount,
     * String tagline
     */
    @Query(" SELECT new movlit.be.movie.presentation.dto.response.MovieDetailResponse "
            + "(m.movieId, m.title, m.originalTitle, m.overview, "
            + "m.popularity, mhc.count, false, m.posterPath, m.backdropPath, "
            + "m.releaseDate, m.productionCountry, m.originalLanguage, "
            + "m.runtime, m.status, m.voteCount, m.tagline) "
            + "FROM MovieEntity m "
            + "LEFT JOIN MovieHeartCountEntity mhc ON mhc.movieId = m.movieId "
            + "WHERE m.movieId = :movieId")
    Optional<MovieDetailResponse> findMovieDetailByMovieId(@Param("movieId") Long movieId);

    /**
     * Long movieId, String title, String originalTitle, String overview,
     * Double popularity, Long heartCount, boolean isHearted, String posterPath,
     * String backdropPath, LocalDate releaseDate, String productionCountry,
     * String originalLanguage, Integer runtime, String status, Long voteCount,
     * String tagline
     */
    @Query("SELECT new movlit.be.movie.presentation.dto.response.MovieDetailResponse "
            + "(m.movieId, m.title, m.originalTitle, m.overview, "
            + "m.popularity, mhc.count, COALESCE(mh.isHearted, false), m.posterPath, m.backdropPath, "
            + "m.releaseDate, m.productionCountry, m.originalLanguage, "
            + "m.runtime, m.status, m.voteCount, m.tagline) "
            + "FROM MovieEntity m "
            + "LEFT JOIN MovieHeartEntity mh ON mh.movieId = m.movieId AND mh.memberId = :currentMemberId"
            + " JOIN MovieHeartCountEntity mhc ON mhc.movieId = m.movieId "
            + "WHERE m.movieId = :movieId")
    Optional<MovieDetailResponse> findMovieDetailByMovieIdAndMemberId(Long movieId, MemberId currentMemberId);

}
