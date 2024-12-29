package movlit.be.movie.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import movlit.be.movie.domain.entity.MovieTagIdREntity;

@Getter
public class MovieTag {

    private MovieTagIdREntity movieTagIdR;
    private Movie movie;
    private String name;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;

}
