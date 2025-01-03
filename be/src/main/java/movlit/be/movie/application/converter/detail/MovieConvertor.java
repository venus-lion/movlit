package movlit.be.movie.application.converter.detail;

import java.time.LocalDateTime;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.request.MovieCommentData;
import movlit.be.movie.presentation.dto.request.MovieCommentDataForDelete;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;

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
                .delYn(false)
                .memberId(data.memberId())
                .movieId(data.movieId())
                .score(data.request().getScore())
                .comment(data.request().getComment())
                .build();
    }

    public static MovieCommentResponse toMovieCommentResponse(MovieCommentId comment) {
        return new MovieCommentResponse(comment);
    }

}
