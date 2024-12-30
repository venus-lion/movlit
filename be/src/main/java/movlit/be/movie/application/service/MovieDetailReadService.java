package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.repository.MovieCrewRepository;
import movlit.be.movie.domain.repository.MovieDetailRepository;
import movlit.be.movie.presentation.dto.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.MovieDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieDetailReadService {

    private final MovieDetailRepository movieDetailRepository;
    private final MovieCrewRepository movieCrewRepository;

    public MovieDetailResponse fetchMovieDetail(Long movieId) {
        return movieDetailRepository.fetchMovieDetailByMovieId(movieId);
    }

    public List<MovieDetailCrewResponse> fetchMovieDetailCrews(Long movieId) {
        return movieCrewRepository.fetchMovieDetailCrewsByMovieId(movieId);
    }

}
