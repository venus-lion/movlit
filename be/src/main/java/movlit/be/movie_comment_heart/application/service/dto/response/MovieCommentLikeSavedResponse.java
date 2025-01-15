package movlit.be.movie_comment_heart.application.service.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;

@NoArgsConstructor
@Getter
public class MovieCommentLikeSavedResponse {

    private MovieCommentId movieCommentId;
    private MovieCommentLikeId movieCommentLikeId;

    private MovieCommentLikeSavedResponse(MovieCommentId movieCommentId, MovieCommentLikeId movieCommentLikeId) {
        this.movieCommentId = movieCommentId;
        this.movieCommentLikeId = movieCommentLikeId;
    }

    public static MovieCommentLikeSavedResponse from(MovieCommentId movieCommentId, MovieCommentLikeId movieCommentLikeId) {
        return new MovieCommentLikeSavedResponse(movieCommentId, movieCommentLikeId);
    }

}
