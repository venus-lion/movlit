package movlit.be.movie_comment_heart.infra;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart.application.service.dto.response.MovieCommentLikeSavedResponse;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart.domain.repository.MovieCommentLikeRepository;
import movlit.be.movie_comment_heart.infra.persistence.MovieCommentLikeJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieCommentLikeRepositoryImpl implements MovieCommentLikeRepository {

    private final MovieCommentLikeJpaRepository movieCommentLikeJpaRepository;

    @Override
    public MovieCommentLikeSavedResponse like(MovieCommentLikeEntity movieCommentLikeEntity) {
        MovieCommentLikeEntity savedEntity = movieCommentLikeJpaRepository.save(movieCommentLikeEntity);
        return MovieCommentLikeSavedResponse.from(savedEntity.getMovieCommentId(), savedEntity.getMovieCommentLikeId());
    }

    @Override
    public void deleteByMovieCommentId(MovieCommentId movieCommentId) {
        movieCommentLikeJpaRepository.deleteByMovieCommentId(movieCommentId);
    }

    @Override
    public boolean existsByMovieCommentIdAndMemberId(MovieCommentId movieCommentId, MemberId memberId) {
        return movieCommentLikeJpaRepository.existsByMovieCommentIdAndMemberId(movieCommentId, memberId);
    }

}
