package movlit.be.movie.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.repository.MovieDetailRepository;
import movlit.be.movie.presentation.dto.MovieDetailResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieDetailReadService {

    private final MovieDetailRepository movieDetailRepository;

    public MovieDetailResponse fetchMovieDetail(Long movieId) {
        return movieDetailRepository.fetchMovieDetailByMovieId(movieId);
    }

}
