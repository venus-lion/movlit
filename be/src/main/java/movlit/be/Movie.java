package movlit.be;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Movie {

    // Discover
    @Id
    private Long id;
    private String title;
    private String originalTitle;
    @Column(length = 60000)
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
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;
    private Long heartCount;

    @Builder
    public Movie(Long id, String title, String originalTitle, String overview, Double popularity, String posterPath,
                 String backdropPath, LocalDate releaseDate, String originalLanguage, Long voteCount,
                 Double voteAverage,
                 String productionCountry, Integer runtime, String status, String tagline, LocalDateTime regDt,
                 LocalDateTime updDt, boolean delYn, Long heartCount) {
        this.id = id;
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
    }

}
