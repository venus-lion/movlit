package movlit.be.movie.infra.persistence.jpa;

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

    Page<MovieEntity> findAllByOrderByVoteCountDescPopularityDesc(Pageable pageable);    // 인기순
//    @Query("SELECT m FROM MovieEntity m LEFT JOIN FETCH m.movieGenreEntityList " +
//            "ORDER BY m.heartCount DESC, m.voteCount DESC, m.popularity DESC")
//    Page<MovieEntity> findAllByOrderByHeartCountDescVoteCountDescPopularityDesc(Pageable pageable);    // 인기순

    // 장르별
    Page<MovieEntity> findByMovieGenreEntityList_MovieGenreIdForEntity_GenreIdOrderByReleaseDateDescPopularityDescVoteCountDesc(
            Long genreId, Pageable pageable);

    @Query("SELECT m FROM MovieEntity m, MovieHeartEntity mh WHERE m.movieId = mh.movieId " +
            "ORDER BY mh.regDt desc LIMIT 1")
    MovieEntity findMostRecentMovieHeart(MemberId memberId);
}
