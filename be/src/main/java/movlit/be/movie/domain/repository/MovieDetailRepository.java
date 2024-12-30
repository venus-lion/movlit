package movlit.be.movie.domain.repository;

import movlit.be.movie.presentation.dto.MovieDetailResponse;

public interface MovieDetailRepository {

    MovieDetailResponse fetchMovieDetailByMovieId(Long movieId);

}
