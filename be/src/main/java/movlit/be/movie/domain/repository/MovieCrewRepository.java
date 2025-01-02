package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;

public interface MovieCrewRepository {

    List<MovieDetailCrewResponse> fetchMovieDetailCrewsByMovieId(Long movieId);

}
