package movlit.be.movie.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.repository.MovieDetailRepository;
import movlit.be.movie.infra.persistence.jpa.MovieDetailJpaRepository;
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

    @Override
    public MovieDetailResponse fetchMovieDetailByMovieIdAndMemberId(Long movieId, MemberId memberId) {
        return movieDetailJpaRepository.findMovieDetailByMovieIdAndMemberId(movieId, memberId)
                .orElseThrow(MovieNotFoundException::new);
    }

}
