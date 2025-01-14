package movlit.be.movie_comment_heart_count.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;
import movlit.be.common.util.ids.MovieHeartCountId;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieCommentLikeCountJpaRepository extends JpaRepository<MovieCommentLikeCountEntity, MovieHeartCountId> {

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

    // FIXME: memberId가 여러개일 경우, list 형태로 반환되니, 여러명이 좋아요를 눌렀을 경우 오류가 발생함
    @Query("SELECT NEW movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse("
            + "mcl.movieCommentLikeId, mcl.movieCommentId, mcl.memberId, mcl.isLiked, mclc.count) "
            + "FROM MovieCommentLikeEntity mcl "
            + "LEFT JOIN MovieCommentLikeCountEntity mclc ON mclc.movieCommentId = mcl.movieCommentId "
            + "WHERE mcl.movieCommentId = :movieCommentId")
    Optional<MovieCommentLikeResponse> findMovieCommentLikeResponse(@Param("movieCommentId") MovieCommentId movieCommentId);

    Optional<MovieCommentLikeCountEntity> findByMovieCommentId(MovieCommentId movieCommentId);

}
