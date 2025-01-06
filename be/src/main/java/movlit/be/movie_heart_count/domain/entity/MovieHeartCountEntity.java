package movlit.be.movie_heart_count.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieHeartCountId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "movie_heart_count")
public class MovieHeartCountEntity {

    @EmbeddedId
    private MovieHeartCountId movieHeartCountId;

    private Long movieId;

    private Long count;

    @Version
    private Long version;

    @Builder
    public MovieHeartCountEntity(MovieHeartCountId movieHeartCountId, Long movieId, Long count) {
        this.movieHeartCountId = movieHeartCountId;
        this.movieId = movieId;
        this.count = count;
    }

}
