package movlit.be.movie.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, Long> {

    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.movieGenreEntityList " +
            "ORDER BY m.releaseDate DESC")
    Page<MovieEntity> findAllByOrderByReleaseDateDesc(Pageable pageable);      // 개봉순

    // 인기순 : VoteCount 500이상의 영화를 popularity 내림차순으로
    Page<MovieEntity> findByVoteCountGreaterThanEqualOrderByPopularityDesc(Long voteCount, Pageable pageable);

    // 장르별
    Page<MovieEntity> findByMovieGenreEntityList_MovieGenreIdForEntity_GenreIdOrderByReleaseDateDescPopularityDescVoteCountDesc(
            Long genreId, Pageable pageable);

    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.movieGenreEntityList "
            + "WHERE m.movieId = :movieId")
    Optional<MovieEntity> findByMovieIdWithGenre(Long movieId);

    @Query("SELECT m FROM MovieEntity m "
            + "LEFT JOIN FETCH m.movieRCrewEntityList mrc "
            + "LEFT JOIN FETCH mrc.movieCrewEntity mc "
            + "WHERE m.movieId = :movieId")
    Optional<MovieEntity> findByIdWithCrew(Long movieId);
}
