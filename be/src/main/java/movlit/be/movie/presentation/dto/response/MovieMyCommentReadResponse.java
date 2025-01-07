package movlit.be.movie.presentation.dto.response;

import movlit.be.common.util.ids.MovieCommentId;

public record MovieMyCommentReadResponse(String nickname, String profileImgUrl, MovieCommentId movieCommentId,
                                         String comment, Double score) {

}
