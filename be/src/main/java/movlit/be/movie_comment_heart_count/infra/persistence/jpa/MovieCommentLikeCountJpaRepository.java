package movlit.be.movie_comment_heart_count.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;
import movlit.be.common.util.ids.MovieHeartCountId;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieCommentLikeCountJpaRepository extends
        JpaRepository<MovieCommentLikeCountEntity, MovieHeartCountId> {

    @Modifying
    @Query("UPDATE MovieCommentLikeCountEntity mclc "
            + "SET mclc.count = mclc.count + 1 "
            + "WHERE mclc.movieCommentId = :movieCommentId")
    void incrementMovieHeartCount(MovieCommentId movieCommentId);

    @Modifying
    @Query("UPDATE MovieCommentLikeCountEntity mclc "
            + "SET mclc.count = mclc.count - 1 "
            + "WHERE mclc.movieCommentId = :movieCommentId")
    void decrementMovieHeartCount(MovieCommentId movieCommentId);

    @Query("SELECT NEW movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse("
            + "mcl.movieCommentLikeId, mcl.movieCommentId, mcl.memberId, mcl.isLiked, mclc.count) "
            + "FROM MovieCommentLikeEntity mcl "
            + "LEFT JOIN MovieCommentLikeCountEntity mclc ON mclc.movieCommentId = mcl.movieCommentId "
            + "WHERE mcl.movieCommentLikeId = :movieCommentLikeId")
    Optional<MovieCommentLikeResponse> findMovieCommentLikeResponse(
            @Param("movieCommentLikeId") MovieCommentLikeId movieCommentLikeId);

    Optional<MovieCommentLikeCountEntity> findByMovieCommentId(MovieCommentId movieCommentId);

}
