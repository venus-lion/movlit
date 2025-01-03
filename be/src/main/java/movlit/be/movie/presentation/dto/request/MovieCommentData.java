package movlit.be.movie.presentation.dto.request;

import movlit.be.common.util.ids.MemberId;

public record MovieCommentData(Long movieId, MemberId memberId, MovieCommentRequest request) {

}
