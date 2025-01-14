package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.movie.domain.repository.MovieCrewRepository;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieCrewReadService {

    private final MovieCrewRepository movieCrewRepository;

    public List<MovieDetailCrewResponse> fetchMovieDetailCrews(Long movieId) {
        return movieCrewRepository.fetchMovieDetailCrewsByMovieId(movieId);
    }

}
