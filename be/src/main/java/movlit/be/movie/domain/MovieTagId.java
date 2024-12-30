package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class MovieTagId implements Serializable {

    private Long movieTagId;
    private Long movieId;

}
