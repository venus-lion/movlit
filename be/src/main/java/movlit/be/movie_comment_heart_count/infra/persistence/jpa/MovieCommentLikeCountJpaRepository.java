package movlit.be.movie_comment_heart_count.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieHeartCountId;
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

    Optional<MovieCommentLikeCountEntity> findByMovieCommentId(MovieCommentId movieCommentId);

}
