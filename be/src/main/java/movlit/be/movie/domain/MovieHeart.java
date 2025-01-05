package movlit.be.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieHeart {
    private MovieHeartId movieHeartId;
    private Long movieId;
    private MemberId memberId;
    private LocalDateTime regDt;
}
