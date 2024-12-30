package movlit.be.movie.domain;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class MovieGenreId implements Serializable {

    private Long genreId;
    private Long movieId;

}