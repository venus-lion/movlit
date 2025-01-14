package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.Movie;
import org.springframework.data.domain.Pageable;

public interface MovieRepository {

    Movie save(Movie movie);

    Movie findById(Long movieId);

    void deleteById(Long movieId);

    List<Movie> findAllOrderByReleaseDateDesc(Pageable pageable);      // 개봉순

//    List<Movie> findAllOrderByHeartCountDescVoteCountDescPopularityDesc(Pageable pageable);    // 인기순

    List<Movie> findByMovieGenreIdForEntity_GenreId(Long genreId, Pageable pageable);

    Movie findMostRecentMovieHeart(MemberId memberId);      // 유저의 가장 최근 찜한 영화

    List<Movie> findByVoteCountGreaterThan500OrderByPopularityDesc(Long minVoteCount, Pageable pageable);

    List<Movie> fetchMovieWithCrewInMovieIds(List<Long> movieId);

}
