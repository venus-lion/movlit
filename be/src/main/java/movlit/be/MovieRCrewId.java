package movlit.be;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class MovieRCrewId implements Serializable {

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_crew_id")
    private Long movieCrewId;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MovieRCrewId that)) {
            return false;
        }
        return Objects.equals(movieCrewId, that.movieCrewId) && Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        int result = movieCrewId.hashCode();
        return 31 * result + movieId.hashCode();
    }

}
