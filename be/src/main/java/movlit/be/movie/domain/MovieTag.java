package movlit.be.movie.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import movlit.be.movie.domain.entity.MovieTagIdForEntity;

@Getter
public class MovieTag {

    private MovieTagIdForEntity movieTagIdR;
    private Movie movie;
    private String name;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;

}
