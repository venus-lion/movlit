package movlit.be.movie.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "movie_tag")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MovieTagEntity {

    @EmbeddedId
    private MovieTagIdForEntity movieTagIdForEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId") // MovieTagId의 필드
    @JoinColumn(name = "movie_id", referencedColumnName = "id", updatable = false, insertable = false)
    private MovieEntity movieEntity;

    private String name;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;

    @Builder
    public MovieTagEntity(MovieTagIdForEntity movieTagIdForEntity, MovieEntity movieEntity, String name,
                          LocalDateTime regDt, LocalDateTime updDt,
                          boolean delYn) {
        this.movieTagIdForEntity = movieTagIdForEntity;
        this.movieEntity = movieEntity;
        this.name = name;
        this.regDt = regDt;
        this.updDt = updDt;
        this.delYn = delYn;
    }

}
