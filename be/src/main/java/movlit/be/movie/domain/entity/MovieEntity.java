package movlit.be.movie.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.member.domain.entity.MemberGenreEntity;

@Table(name = "movie")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MovieEntity {

    // Discover
    @Id
    @Column(name = "id")
    private Long movieId;
    private String title;
    private String originalTitle;

    @Lob
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

    @OneToMany(mappedBy = "movieEntity")
    private List<MovieRCrewEntity> movieRCrewEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "movieEntity")
    private List<MovieGenreEntity> movieGenreEntities = new ArrayList<>();

    @Builder
    public MovieEntity(Long movieId, String title, String originalTitle, String overview, Double popularity,
                       String posterPath, String backdropPath, LocalDate releaseDate, String originalLanguage,
                       Long voteCount, Double voteAverage, String productionCountry, Integer runtime, String status,
                       String tagline, LocalDateTime regDt, LocalDateTime updDt, boolean delYn, Long heartCount,
                       List<MovieRCrewEntity> movieRCrewEntityList) {
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
        this.movieRCrewEntityList = movieRCrewEntityList;
    }

}