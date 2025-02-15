package movlit.be.movie.application.converter.detail;

import java.time.LocalDateTime;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.request.MovieCommentDataForDelete;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;
import movlit.be.movie_heart.presentation.dto.response.MovieHeartResponse;
import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;

public class MovieConvertor {

    private MovieConvertor() {
        // TODO:
    }

    public static MovieCommentDataForDelete toMovieDetailCommentDataForDelete(MemberId memberId,
                                                                              MovieCommentId movieCommentId) {
        return new MovieCommentDataForDelete(memberId, movieCommentId);
    }

    public static MovieCommentEntity toMovieCommentEntity(Long movieId, MemberId memberId, String comment, Double score,
                                                          MovieCommentId movieCommentId, LocalDateTime now) {
        return MovieCommentEntity.builder()
                .movieCommentId(movieCommentId)
                .memberId(memberId)
                .movieId(movieId)
                .score(score)
                .comment(comment)
                .regDt(now)
                .build();
    }

    public static MovieHeartResponse toMovieHeartResponse(MovieHeartEntity movieHeartEntity, Long movieHeartCount) {
        return MovieHeartResponse.builder()
                .movieHeartId(movieHeartEntity.getMovieHeartId())
                .isHearted(movieHeartEntity.isHearted())
                .movieId(movieHeartEntity.getMovieId())
                .memberId(movieHeartEntity.getMemberId())
                .movieHeartCnt(movieHeartCount)
                .build();
    }

    public static MovieHeartEntity toMovieHeartEntity(Long movieId, MemberId memberId) {
        return MovieHeartEntity.builder()
                .movieHeartId(IdFactory.createMovieHeartId())
                .movieId(movieId)
                .memberId(memberId)
                .isHearted(true)
                .build();
    }

    public static MovieCommentLikeEntity makeMovieCommentLikeEntity(MemberId memberId,
                                                                    MovieCommentId movieCommentId) {
        return MovieCommentLikeEntity.builder()
                .movieCommentLikeId(IdFactory.createMovieCommentLikeId())
                .movieCommentId(movieCommentId)
                .memberId(memberId)
                .isLiked(true)
                .build();
    }

    public static MovieHeartCountEntity toMovieHeartCountEntity(Long movieId) {
        return MovieHeartCountEntity.builder()
                .movieHeartCountId(IdFactory.createMovieHeartCountId())
                .movieId(movieId)
                .count(0L)
                .build();
    }

    public static MovieCommentLikeCountEntity makeMovieCommentLikeCountEntity(MovieCommentId movieCommentId) {
        return MovieCommentLikeCountEntity.builder()
                .movieCommentLikeCountId(IdFactory.createMovieCommentLikeCountId())
                .movieCommentId(movieCommentId)
                .count(0L)
                .build();
    }

}
