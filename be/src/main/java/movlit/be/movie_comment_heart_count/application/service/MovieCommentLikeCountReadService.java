package movlit.be.movie_comment_heart_count.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.MovieCommentLikeCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MovieCommentLikeCountReadService {

    private final MovieCommentLikeCountRepository movieCommentLikeCountRepository;

    public MovieCommentLikeResponse fetchMovieCommentLikeResponseByMovieCommentId(MovieCommentId movieCommentId) {
        return movieCommentLikeCountRepository.fetchMovieCommentLikeResponseByMovieCommentId(movieCommentId);
    }

}
