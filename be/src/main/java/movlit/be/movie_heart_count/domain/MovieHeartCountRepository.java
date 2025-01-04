package movlit.be.movie_heart_count.domain;

import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;

public interface MovieHeartCountRepository {

    MovieHeartCountEntity save(MovieHeartCountEntity movieHeartCountEntity);

    void incrementMovieHeartCount(Long movieId);

    void decrementMovieHeartCount(Long movieId);

    Long fetchMovieHeartCountByMovieId(Long movieId);

}
