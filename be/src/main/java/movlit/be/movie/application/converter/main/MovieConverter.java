package movlit.be.movie.application.converter.main;

import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.entity.MovieEntity;

public class MovieConverter {
    // Domain -> Entity
    public static MovieEntity toEntity(Movie movie) {
        return MovieEntity.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .originalTitle(movie.getOriginalTitle())
                .overview(movie.getOverview())
                .popularity(movie.getPopularity())
                .posterPath(movie.getPosterPath())
                .backdropPath(movie.getBackdropPath())
                .releaseDate(movie.getReleaseDate())
                .originalLanguage(movie.getOriginalLanguage())
                .voteCount(movie.getVoteCount())
                .voteAverage(movie.getVoteAverage())
                .productionCountry(movie.getProductionCountry())
                .runtime(movie.getRuntime())
                .status(movie.getStatus())
                .tagline(movie.getTagline())
                .regDt(movie.getRegDt())
                .updDt(movie.getUpdDt())
                .delYn(movie.isDelYn())
                .heartCount(movie.getHeartCount())
                .build();
    }

    // Entity -> Domain
    public static Movie toDomain(MovieEntity movieEntity){
        return Movie.builder()
                .movieId(movieEntity.getMovieId())
                .title(movieEntity.getTitle())
                .originalTitle(movieEntity.getOriginalTitle())
                .overview(movieEntity.getOverview())
                .popularity(movieEntity.getPopularity())
                .posterPath(movieEntity.getPosterPath())
                .backdropPath(movieEntity.getBackdropPath())
                .releaseDate(movieEntity.getReleaseDate())
                .originalLanguage(movieEntity.getOriginalLanguage())
                .voteCount(movieEntity.getVoteCount())
                .voteAverage(movieEntity.getVoteAverage())
                .productionCountry(movieEntity.getProductionCountry())
                .runtime(movieEntity.getRuntime())
                .status(movieEntity.getStatus())
                .tagline(movieEntity.getTagline())
                .regDt(movieEntity.getRegDt())
                .updDt(movieEntity.getUpdDt())
                .delYn(movieEntity.isDelYn())
                .heartCount(movieEntity.getHeartCount())
                .build();
    }
}
