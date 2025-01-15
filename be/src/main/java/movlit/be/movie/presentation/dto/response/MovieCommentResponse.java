package movlit.be.movie.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;

@NoArgsConstructor
@Getter
public class MovieCommentResponse {

    private MovieCommentId commentId;

    private MovieCommentResponse(MovieCommentId commentId) {
        this.commentId = commentId;
    }

    public static MovieCommentResponse of(MovieCommentId movieCommentId) {
        return new MovieCommentResponse(movieCommentId);
    }

}
