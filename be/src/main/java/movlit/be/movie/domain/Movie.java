package movlit.be.movie.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Movie {

    // Discover
    private Long movieId;
    private String title;
    private String originalTitle;

    private String overview;
    private Double popularity;
    private String posterPath;
    private String backdropPath;
    private LocalDate releaseDate;
    private String originalLanguage;
    private Long voteCount;
    private Double voteAverage;

    // Detail
    private String productionCountry;
    private Integer runtime;
    private String status;
    private String tagline;

    // Custom
    // Time 분리
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;
    private Long heartCount;

    private List<MovieRCrew> movieRCrewList = new ArrayList<>();

    @Builder
    public Movie(Long movieId, String title, String originalTitle, String overview, Double popularity,
                 String posterPath,
                 String backdropPath, LocalDate releaseDate, String originalLanguage, Long voteCount,
                 Double voteAverage,
                 String productionCountry, Integer runtime, String status, String tagline, LocalDateTime regDt,
                 LocalDateTime updDt, boolean delYn, Long heartCount, List<MovieRCrew> movieRCrewList) {
        this.movieId = movieId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.productionCountry = productionCountry;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.regDt = regDt;
        this.updDt = updDt;
        this.delYn = delYn;
        this.heartCount = heartCount;
        this.movieRCrewList = movieRCrewList;
    }

}