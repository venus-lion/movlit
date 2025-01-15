package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.movie.domain.repository.MovieGenreRepository;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieGenreReadService {

    private final MovieGenreRepository movieGenreRepository;

    public List<MovieDetailGenreResponse> fetchMovieDetailGenres(Long movieId) {
        return movieGenreRepository.fetchMovieDetailGenreNamesByMovieId(movieId);
    }

}
