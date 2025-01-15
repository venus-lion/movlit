package movlit.be.movie_comment_heart_count.domain;

import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;

public interface MovieCommentLikeCountRepository {

    MovieCommentLikeCountEntity save(MovieCommentLikeCountEntity movieHeartCountEntity);

    void incrementMovieHeartCount(MovieCommentId movieCommentId);

    void decrementMovieHeartCount(MovieCommentId movieCommentId);

    MovieCommentLikeResponse fetchMovieCommentLikeResponse(MovieCommentLikeId movieCommentLikeId);

}
