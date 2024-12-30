package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.Getter;
import movlit.be.common.util.ids.MovieId;

@Getter
public class MovieTagId implements Serializable {

    private Long movieTagId;
    private MovieId movieId;

}
