package movlit.be.movie.application.converter.main;

import movlit.be.movie.domain.MovieTag;
import movlit.be.movie.domain.entity.MovieTagEntity;
import movlit.be.movie.domain.entity.MovieTagIdForEntity;

public class MovieTagConverter {
    // Domain -> Entity

    // Entity -> Domain
    public static MovieTag toDomain(MovieTagEntity movieTagEntity) {
        MovieTagIdForEntity movieTagIdForEntity = movieTagEntity.getMovieTagIdForEntity();
        return MovieTag.builder()
                .movieTagId(movieTagIdForEntity.getMovieTagId())
                .movieId(movieTagIdForEntity.getMovieId())
                .name(movieTagEntity.getName())
                .build();
    }

}
