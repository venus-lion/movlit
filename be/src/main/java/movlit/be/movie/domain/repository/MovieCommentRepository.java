package movlit.be.movie.domain.repository;

import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;

public interface MovieCommentRepository {

    MovieCommentId createComment(MovieCommentEntity movieCommentEntity);

}
