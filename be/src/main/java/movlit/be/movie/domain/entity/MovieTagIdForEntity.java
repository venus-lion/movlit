package movlit.be.movie.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class MovieTagIdForEntity implements Serializable {

    @Column(name = "movie_tag_id")
    private Long movieTagId;

    @Column(name = "movie_id")
    private Long movieId;

    public MovieTagIdForEntity(Long movieTagId, Long movieId) {
        this.movieTagId = movieTagId;
        this.movieId = movieId;
    }

}
