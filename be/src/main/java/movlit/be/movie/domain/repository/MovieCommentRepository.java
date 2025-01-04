package movlit.be.movie.domain.repository;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MovieCommentRepository {

    MovieCommentId createComment(MovieCommentEntity movieCommentEntity);

    void deleteComment(MovieCommentId movieCommentId);

    MovieCommentEntity fetchById(MovieCommentId movieCommentId);

    Slice<MovieCommentReadResponse> fetchComments(Long movieId, MemberId memberId, Pageable pageable);

    Slice<MovieCommentReadResponse> fetchComments(Long movieId, Pageable pageable);

    Optional<MovieCommentEntity> fetchByMemberIdAndMovieId(MemberId memberId, Long movieId);

}
