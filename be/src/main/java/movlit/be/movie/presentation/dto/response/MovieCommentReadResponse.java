package movlit.be.movie.presentation.dto.response;

import movlit.be.common.util.ids.MovieCommentId;

public record MovieCommentReadResponse(MovieCommentId movieCommentId, Double score, String comment,
                                       String nickname, String profileImgUrl, Long commentCount,
                                       boolean isLiked, Long commentLikeCount) {

}
