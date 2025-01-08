package movlit.be.movie.domain.repository;

import movlit.be.common.util.Genre;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.document.MovieDocument;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieSearchRepository {
    List<Movie> searchByUserInterestGenre(List<Genre> genreList, Pageable pageable);

    List<Movie> searchByUserHeartMovieAndCrew(List<Movie> movieList, Pageable pageable);

    List<MovieDocument> searchMovieList(String inputStr, Pageable pageable);
}
