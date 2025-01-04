package movlit.be.movie_comment_heart.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;

public interface MovieCommentLikeRepository {

    MovieCommentLikeEntity like(MovieCommentLikeEntity movieCommentLikeEntity);

    void deleteByMovieCommentId(MovieCommentId movieCommentId);

    boolean existsByMovieCommentIdAndMemberId(MovieCommentId movieCommentId, MemberId memberId);

}
