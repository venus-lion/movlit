package movlit.be.movie.domain.repository;

import movlit.be.common.util.Genre;
import movlit.be.movie.domain.Movie;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieSearchRepository {
    List<Movie> searchByUserInterestGenre(List<Genre> genreList, Pageable pageable);

    List<Movie> searchByUserHeartMovieAndCrew(Movie movie, Pageable pageable);

    List<Movie> searchByUserHeartMovie(Movie movie, Pageable pageable);
}
