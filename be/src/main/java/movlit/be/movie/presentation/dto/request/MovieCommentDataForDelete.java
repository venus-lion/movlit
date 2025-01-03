package movlit.be.movie.presentation.dto.request;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;

public record MovieCommentDataForDelete(MemberId currentMemberId,
                                        MovieCommentId movieCommentId) {

}
