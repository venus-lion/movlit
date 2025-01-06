package movlit.be.movie.application.converter.main;

import java.util.Objects;
import java.util.stream.Collectors;
import movlit.be.common.util.Genre;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.MovieGenre;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.document.MovieGenreForDocument;
import movlit.be.movie.domain.document.MovieTagForDocument;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.entity.MovieGenreEntity;
import movlit.be.movie.domain.entity.MovieTagEntity;

public class MovieDocumentConverter {

    // MovieEntity -> MovieDocument
    public static MovieDocument entityToDocument(MovieEntity movieEntity) {
        if (Objects.isNull(movieEntity)) {
            return null;
        }

        return MovieDocument.builder()
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
                .delYn(movieEntity.isDelYn())
                .movieGenre(
                        movieEntity.getMovieGenreEntityList().stream()
                                .map(MovieDocumentConverter::entityToForDocument)
                                .collect(Collectors.toList())
                )
                .movieTag(
                        movieEntity.getMovieTagEntityList().stream()
                                .map(MovieDocumentConverter::entityToForDocument)
                                .collect(Collectors.toList())
                )
                .build();
    }

    // MovieGenreEntity -> MovieGenreForDocument
    public static MovieGenreForDocument entityToForDocument(MovieGenreEntity movieGenreEntity) {
        Long genreId = movieGenreEntity.getMovieGenreIdForEntity().getGenreId();

        return MovieGenreForDocument.builder()
                .genreId(genreId)
                .genreName(Genre.of(genreId).getName())
                .build();
    }

    // MovieTagEntity -> MovieTagForDocument
    public static MovieTagForDocument entityToForDocument(MovieTagEntity movieTagEntity) {
        Long movieTagId = movieTagEntity.getMovieTagIdForEntity().getMovieTagId();

        return MovieTagForDocument.builder()
                .movieTagId(movieTagId)
                .tagName(movieTagEntity.getName())
                .build();
    }

    // Document -> Domain
    public static Movie documentToDomain(MovieDocument movieDocument) {
        return Movie.builder()
                .movieId(movieDocument.getMovieId())
                .title(movieDocument.getTitle())
                .originalTitle(movieDocument.getOriginalTitle())
                .overview(movieDocument.getOverview())
                .popularity(movieDocument.getPopularity())
                .posterPath(movieDocument.getPosterPath())
                .backdropPath(movieDocument.getBackdropPath())
                .releaseDate(movieDocument.getReleaseDate())
                .originalLanguage(movieDocument.getOriginalLanguage())
                .voteCount(movieDocument.getVoteCount())
                .voteAverage(movieDocument.getVoteAverage())
                .productionCountry(movieDocument.getProductionCountry())
                .runtime(movieDocument.getRuntime())
                .tagline(movieDocument.getTagline())
                .delYn(movieDocument.isDelYn())
                .movieGenreList(movieDocument.getMovieGenre().stream()
                        .map(x -> new MovieGenre(x.getGenreId(), x.getGenreName()))
                        .toList()
                )
                .build();
    }

}
