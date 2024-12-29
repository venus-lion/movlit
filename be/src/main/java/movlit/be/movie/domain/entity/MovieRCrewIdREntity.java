package movlit.be.movie.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieCrewId;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class MovieRCrewIdREntity implements Serializable {

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_crew_id")
    private MovieCrewId movieCrewId;

    public MovieRCrewIdREntity(Long movieId, MovieCrewId movieCrewId) {
        this.movieId = movieId;
        this.movieCrewId = movieCrewId;
    }

}