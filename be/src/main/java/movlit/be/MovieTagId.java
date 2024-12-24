package movlit.be;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class MovieTagId implements Serializable {
    private Long movieTagId;
    private Long movieId;

    @Builder
    public MovieTagId(Long movieTagId, Long movieId) {
        this.movieTagId = movieTagId;
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieTagId that = (MovieTagId) o;
        return Objects.equals(movieTagId, that.movieTagId) && Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieTagId, movieId);
    }

}
