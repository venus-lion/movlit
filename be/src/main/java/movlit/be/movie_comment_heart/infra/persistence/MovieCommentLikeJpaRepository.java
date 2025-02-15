package movlit.be.movie_comment_heart.infra.persistence;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieHeartId;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCommentLikeJpaRepository extends JpaRepository<MovieCommentLikeEntity, MovieHeartId> {

    boolean existsByMovieCommentIdAndMemberId(MovieCommentId movieCommentId, MemberId memberId);

    void deleteByMovieCommentId(MovieCommentId movieCommentId);

}
