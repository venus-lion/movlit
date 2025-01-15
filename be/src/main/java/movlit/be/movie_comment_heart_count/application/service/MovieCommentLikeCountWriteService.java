package movlit.be.movie_comment_heart_count.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart_count.domain.MovieCommentLikeCountRepository;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MovieCommentLikeCountWriteService {

    private final MovieCommentLikeCountRepository movieCommentLikeCountRepository;

    public void save(MovieCommentLikeCountEntity movieCommentLikeCountEntity) {
        movieCommentLikeCountRepository.save(movieCommentLikeCountEntity);
    }

    public void incrementMovieCommentLikeCount(MovieCommentId movieCommentId) {
        movieCommentLikeCountRepository.incrementMovieHeartCount(movieCommentId);
    }

    public void decrementMovieCommentLikeCount(MovieCommentId movieCommentId) {
        movieCommentLikeCountRepository.decrementMovieHeartCount(movieCommentId);
    }

}
