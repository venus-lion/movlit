package movlit.be.movie.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.domain.repository.MovieDetailRepository;
import movlit.be.movie.infra.persistence.jpa.MovieDetailJpaRepository;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MovieDetailRepositoryImpl implements MovieDetailRepository {

    private final MovieDetailJpaRepository movieDetailJpaRepository;

    @Override
    public MovieDetailResponse fetchMovieDetailByMovieId(Long movieId) {
        return movieDetailJpaRepository.findMovieDetailByMovieId(movieId)
                .orElseThrow(MovieNotFoundException::new);
    }

}
