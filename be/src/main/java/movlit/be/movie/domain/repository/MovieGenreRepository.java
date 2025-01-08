package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.common.util.Genre;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;

public interface MovieGenreRepository {

    List<MovieDetailGenreResponse> fetchMovieDetailGenreNamesByMovieId(Long movieId);

    List<Genre> fetchMovieDetailGenresByMovieId(Long movieId);

}
