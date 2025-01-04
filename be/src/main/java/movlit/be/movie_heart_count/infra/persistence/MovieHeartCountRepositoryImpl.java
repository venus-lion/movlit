package movlit.be.movie_heart_count.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieHeartNotFoundException;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.movie_heart_count.domain.MovieHeartCountRepository;
import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;
import movlit.be.movie_heart_count.infra.persistence.jpa.MovieHeartCountJpaRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MovieHeartCountRepositoryImpl implements MovieHeartCountRepository {

    private final MovieHeartCountJpaRepository movieHeartCountJpaRepository;

    @Override
    public MovieHeartCountEntity save(MovieHeartCountEntity movieHeartCountEntity) {
        return movieHeartCountJpaRepository.save(movieHeartCountEntity);
    }

    @Override
    public void incrementMovieHeartCount(Long movieId) {
        movieHeartCountJpaRepository.incrementMovieHeartCount(movieId);
    }

    @Override
    public Long fetchMovieHeartCountByMovieId(Long movieId) {
        MovieHeartCountEntity movieHeartCountEntity = movieHeartCountJpaRepository.findByMovieId(movieId)
                .orElseThrow(MovieHeartNotFoundException::new);
        return movieHeartCountEntity.getCount();
    }

}
