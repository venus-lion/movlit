package movlit.be.movie_heart.presentation.dto.response;

import lombok.Builder;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;

@Builder
public record MovieHeartResponse(MovieHeartId movieHeartId, Long movieId, MemberId memberId, boolean isHearted,
                                 Long movieHeartCnt) {

}
