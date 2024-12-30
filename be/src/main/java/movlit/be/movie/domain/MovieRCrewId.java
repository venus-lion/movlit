package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.Getter;
import movlit.be.common.util.ids.MovieCrewId;

@Getter
public class MovieRCrewId implements Serializable {

    private Long movieId;
    private MovieCrewId movieCrewId;

}