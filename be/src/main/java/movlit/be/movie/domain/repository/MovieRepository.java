package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.movie.domain.Movie;
import org.springframework.data.domain.Pageable;

public interface MovieRepository {

    Movie save(Movie movie);

    Movie findById(Long movieId);

    void deleteById(Long movieId);

    List<Movie> findAllOrderByReleaseDateDesc(Pageable pageable);      // 개봉순

    List<Movie> findAllOrderByHeartCountDescVoteCountDescPopularityDesc(Pageable pageable);    // 인기순

    List<Movie> findByMovieGenreIdForEntity_GenreId(Long genreId, Pageable pageable);
}
