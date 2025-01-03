package movlit.be.movie.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;

public interface MovieCommentRepository {

    MovieCommentId createComment(MovieCommentEntity movieCommentEntity);

    void deleteComment(MovieCommentId movieCommentId);

    MovieCommentEntity fetchById(MovieCommentId movieCommentId);

}
