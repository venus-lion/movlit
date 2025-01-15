package movlit.be.movie_comment_heart_count.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MovieCommentLikeId;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.MovieCommentLikeCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MovieCommentLikeCountReadService {

    private final MovieCommentLikeCountRepository movieCommentLikeCountRepository;

    public MovieCommentLikeResponse fetchMovieCommentLikeResponse(MovieCommentLikeId movieCommentLikeId) {
        return movieCommentLikeCountRepository.fetchMovieCommentLikeResponse(movieCommentLikeId);
    }

}
