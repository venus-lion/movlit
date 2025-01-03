package movlit.be.movie.presentation.dto.response;

import movlit.be.common.util.ids.MovieCommentId;

public record MovieCommentReadResponse(MovieCommentId movieCommentId, Integer score, String comment,
                                       String nickname, String profileImgUrl) {

}
