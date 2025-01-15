package movlit.be.movie.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import movlit.be.movie.presentation.dto.response.MovieMyCommentReadResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MovieCommentRepository {

    MovieCommentResponse createComment(MovieCommentEntity movieCommentEntity);

    void deleteComment(MovieCommentId movieCommentId);

    MovieCommentEntity fetchById(MovieCommentId movieCommentId);

    Slice<MovieCommentReadResponse> fetchComments(Long movieId, MemberId memberId, Pageable pageable);

    Slice<MovieCommentReadResponse> fetchComments(Long movieId, Pageable pageable);

    boolean existsByMemberIdAndMovieId(MemberId memberId, Long movieId);

    MovieMyCommentReadResponse fetchMyComment(Long movieId, MemberId currentMemberId);

}
