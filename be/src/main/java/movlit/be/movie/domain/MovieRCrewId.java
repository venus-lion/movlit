package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import movlit.be.common.util.ids.MovieCrewId;

@Getter
@AllArgsConstructor
public class MovieRCrewId implements Serializable {

    private Long movieId;
    private MovieCrewId movieCrewId;

}