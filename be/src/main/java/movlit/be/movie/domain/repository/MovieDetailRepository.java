package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.movie.presentation.dto.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.MovieDetailResponse;

public interface MovieDetailRepository {

    MovieDetailResponse fetchMovieDetailByMovieId(Long movieId);



}
