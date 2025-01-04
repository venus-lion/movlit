package movlit.be.movie_heart_count.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.movie_heart_count.domain.MovieHeartCountRepository;
import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MovieHeartCountService {

    private final MovieHeartCountRepository movieHeartCountRepository;

    @Transactional
    public void save(MovieHeartCountEntity movieHeartCountEntity) {
        movieHeartCountRepository.save(movieHeartCountEntity);
    }

    @Transactional
    public void incrementMovieHeartCount(Long movieId) {
        movieHeartCountRepository.incrementMovieHeartCount(movieId);
    }

    @Transactional
    public void decrementMovieHeartCount(Long movieId) {
        movieHeartCountRepository.decrementMovieHeartCount(movieId);
    }

    public Long fetchMovieHeartCountByMovieId(Long movieId) {
        return movieHeartCountRepository.fetchMovieHeartCountByMovieId(movieId);
    }

}
