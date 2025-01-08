package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.common.util.Genre;
import movlit.be.movie.domain.Movie;
import org.springframework.data.domain.Pageable;

public interface MovieSearchRepository {

    List<Movie> searchInterestGenre(List<Genre> genreList, Pageable pageable);

    List<Movie> searchByUserHeartMovieAndCrew(List<Movie> movieList, Pageable pageable);

}
