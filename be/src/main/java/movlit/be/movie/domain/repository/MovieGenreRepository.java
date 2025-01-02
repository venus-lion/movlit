package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;

public interface MovieGenreRepository {

    List<MovieDetailGenreResponse> fetchMovieDetailGenresByMovieId(Long movieId);

}
