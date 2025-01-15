package movlit.be.movie_comment_heart_count.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieCommentLikeNotFoundException;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.MovieCommentLikeCountRepository;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import movlit.be.movie_comment_heart_count.infra.persistence.jpa.MovieCommentLikeCountJpaRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MovieCommentLikeCountRepositoryImpl implements MovieCommentLikeCountRepository {

    private final MovieCommentLikeCountJpaRepository movieCommentLikeCountJpaRepository;

    @Override
    public MovieCommentLikeCountEntity save(MovieCommentLikeCountEntity movieHeartCountEntity) {
        return movieCommentLikeCountJpaRepository.save(movieHeartCountEntity);
    }

    @Override
    public void incrementMovieHeartCount(MovieCommentId movieCommentId) {
        movieCommentLikeCountJpaRepository.incrementMovieHeartCount(movieCommentId);
    }

    @Override
    public void decrementMovieHeartCount(MovieCommentId movieCommentId) {
        movieCommentLikeCountJpaRepository.decrementMovieHeartCount(movieCommentId);
    }

    @Override
    public MovieCommentLikeResponse fetchMovieCommentLikeResponse(MovieCommentLikeId movieCommentLikeId) {
        return movieCommentLikeCountJpaRepository.  findMovieCommentLikeResponse(movieCommentLikeId)
                .orElseThrow(MovieCommentLikeNotFoundException::new);
    }

}
