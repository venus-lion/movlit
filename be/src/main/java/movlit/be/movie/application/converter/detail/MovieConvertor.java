package movlit.be.movie.application.converter.detail;

import java.time.LocalDateTime;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.request.MovieCommentData;
import movlit.be.movie.presentation.dto.request.MovieCommentDataForDelete;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;
import movlit.be.movie_heart.presentation.dto.response.MovieHeartResponse;
import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;

public class MovieConvertor {

    private MovieConvertor() {
        // TODO:
    }

    public static MovieCommentData toMovieDetailCommentData(Long movieId, MemberId memberId,
                                                            MovieCommentRequest request) {
        return new MovieCommentData(movieId, memberId, request);
    }

    public static MovieCommentDataForDelete toMovieDetailCommentDataForDelete(MemberId memberId,
                                                                              MovieCommentId movieCommentId) {
        return new MovieCommentDataForDelete(memberId, movieCommentId);
    }

    public static MovieCommentEntity toMovieCommentEntity(MovieCommentData data, MovieCommentId movieCommentId,
                                                          LocalDateTime now) {
        return MovieCommentEntity.builder()
                .movieCommentId(movieCommentId)
                .memberId(data.memberId())
                .movieId(data.movieId())
                .score(data.request().getScore())
                .comment(data.request().getComment())
                .build();
    }

    public static MovieCommentResponse toMovieCommentResponse(MovieCommentId comment) {
        return new MovieCommentResponse(comment);
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

    public static MovieCommentLikeResponse toMovieCommentLikeResponse(MovieCommentLikeEntity movieCommentLikeEntity,
                                                                      Long movieCommentLikeCount) {
        return MovieCommentLikeResponse.builder()
                .movieCommentLikeId(movieCommentLikeEntity.getMovieCommentLikeId())
                .movieCommentId(movieCommentLikeEntity.getMovieCommentId())
                .memberId(movieCommentLikeEntity.getMemberId())
                .isLiked(movieCommentLikeEntity.isLiked())
                .movieCommentLikeCount(movieCommentLikeCount)
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

    public static MovieCommentLikeEntity toMovieCommentLikeEntity(MemberId memberId,
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

    public static MovieCommentLikeCountEntity toMovieCommentLikeCountEntity(MovieCommentEntity movieCommentEntity) {
        return MovieCommentLikeCountEntity.builder()
                .movieCommentLikeCountId(IdFactory.createMovieCommentLikeCountId())
                .movieCommentId(movieCommentEntity.getMovieCommentId())
                .count(0L)
                .build();
    }

}
