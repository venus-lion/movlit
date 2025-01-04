package movlit.be.movie_comment_heart.presentation.dto.response;

import lombok.Builder;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;

@Builder
public record MovieCommentLikeResponse(MovieCommentLikeId movieCommentLikeId, MovieCommentId movieCommentId,
                                       MemberId memberId, boolean isLiked, Long movieCommentLikeCount) {

}
