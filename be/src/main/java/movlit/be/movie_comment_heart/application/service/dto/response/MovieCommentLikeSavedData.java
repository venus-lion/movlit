package movlit.be.movie_comment_heart.application.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MovieCommentLikeSavedData {

    private MovieCommentId movieCommentId;
    private MovieCommentLikeId movieCommentLikeId;

    private MovieCommentLikeSavedData(MovieCommentId movieCommentId, MovieCommentLikeId movieCommentLikeId) {
        this.movieCommentId = movieCommentId;
        this.movieCommentLikeId = movieCommentLikeId;
    }

    public static MovieCommentLikeSavedData from(MovieCommentId movieCommentId, MovieCommentLikeId movieCommentLikeId) {
        return new MovieCommentLikeSavedData(movieCommentId, movieCommentLikeId);
    }

}
