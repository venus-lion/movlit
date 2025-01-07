package movlit.be.movie.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.movie.domain.entity.MovieTagIdForEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieTag {

    private Long movieTagId;
    private Long movieId;
    private String name;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;

}
