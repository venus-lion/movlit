package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.Getter;
import movlit.be.common.util.ids.MovieId;
import movlit.be.common.util.ids.MovieTagId;

@Getter
public class MovieTagIdR implements Serializable {

    private MovieTagId movieTagId;
    private MovieId movieId;

}
