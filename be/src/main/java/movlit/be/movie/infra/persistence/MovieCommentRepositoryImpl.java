package movlit.be.movie.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieCommentNotFound;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.infra.persistence.jpa.MovieCommentJpaRepository;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import movlit.be.movie.presentation.dto.response.MovieMyCommentReadResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieCommentRepositoryImpl implements MovieCommentRepository {

    private final MovieCommentJpaRepository movieCommentJpaRepository;

    @Override
    public MovieCommentResponse createComment(MovieCommentEntity movieCommentEntity) {
        MovieCommentEntity savedMovieCommentEntity = movieCommentJpaRepository.save(movieCommentEntity);
        return MovieCommentResponse.of(savedMovieCommentEntity.getMovieCommentId());
    }

    @Override
    public void deleteComment(MovieCommentId movieCommentId) {
        movieCommentJpaRepository.deleteById(movieCommentId);
    }

    @Override
    public MovieCommentEntity fetchById(MovieCommentId movieCommentId) {
        return movieCommentJpaRepository.findById(movieCommentId)
                .orElseThrow(MovieCommentNotFound::new);
    }

    @Override
    public Slice<MovieCommentReadResponse> fetchComments(Long movieId, Pageable pageable) {
        return movieCommentJpaRepository.findAllComment(movieId, pageable);
    }

    @Override
    public Slice<MovieCommentReadResponse> fetchComments(Long movieId, MemberId memberId, Pageable pageable) {
        return movieCommentJpaRepository.findAllCommentsWithMemberId(movieId, memberId, pageable);
    }

    @Override
    public boolean existsByMemberIdAndMovieId(MemberId memberId, Long movieId) {
        return movieCommentJpaRepository.existsByMemberIdAndMovieId(memberId, movieId);
    }

    @Override
    public MovieMyCommentReadResponse fetchMyComment(Long movieId, MemberId currentMemberId) {
        return movieCommentJpaRepository.findMyComment(movieId, currentMemberId);
    }

}
