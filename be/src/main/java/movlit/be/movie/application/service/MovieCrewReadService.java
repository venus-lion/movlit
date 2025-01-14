package movlit.be.movie.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.movie.domain.repository.MovieCrewRepository;
import movlit.be.movie.presentation.dto.response.MovieCrewResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieCrewReadService {
    private final MovieCrewRepository movieCrewRepository;

    @Transactional(readOnly = true)
    public List<MovieCrewResponseDto> fetchMovieCrewByMovieId(List<Long> movieIds) {
        return movieCrewRepository.fetchMovieCrewByMovieIds(movieIds);
    }
}
