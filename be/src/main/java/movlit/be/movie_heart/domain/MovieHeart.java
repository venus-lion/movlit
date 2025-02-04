package movlit.be.movie_heart.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieHeart {

    private MovieHeartId movieHeartId;
    private Long movieId;
    private MemberId memberId;
    private boolean isHearted;
    private LocalDateTime regDt;

}
