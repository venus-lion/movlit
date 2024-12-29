package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.Getter;
import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.common.util.ids.MovieId;

@Getter
public class MovieRCrewIdR implements Serializable {

    private MovieId movieId;
    private MovieCrewId movieCrewId;

}