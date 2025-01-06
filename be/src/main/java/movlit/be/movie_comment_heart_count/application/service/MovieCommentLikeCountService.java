package movlit.be.movie_comment_heart_count.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart_count.domain.MovieCommentLikeCountRepository;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MovieCommentLikeCountService {

    private final MovieCommentLikeCountRepository movieCommentLikeCountRepository;

    @Transactional
    public void save(MovieCommentLikeCountEntity movieCommentLikeCountEntity) {
        movieCommentLikeCountRepository.save(movieCommentLikeCountEntity);
    }

    @Transactional
    public void incrementMovieCommentLikeCount(MovieCommentId movieCommentId) {
        movieCommentLikeCountRepository.incrementMovieHeartCount(movieCommentId);
    }

    @Transactional
    public void decrementMovieCommentLikeCount(MovieCommentId movieCommentId) {
        movieCommentLikeCountRepository.decrementMovieHeartCount(movieCommentId);
    }

    public Long fetchMovieCommentLikeCountByMovieIdAndMovieCommentId(MovieCommentId movieCommentId) {
        return movieCommentLikeCountRepository.fetchMovieCommentLikeCountByMovieCommentId(movieCommentId);
    }

}
