package movlit.be.movie.application.service;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberExistsInMovieCommentException;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import movlit.be.movie_comment_heart_count.application.service.MovieCommentLikeCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieDetailWriteService {

    private final MovieCommentRepository movieCommentRepository;
    private final MovieDetailReadService movieDetailReadService;
    private final MovieCommentLikeCountService movieCommentLikeCountService;

    public MovieCommentResponse createComment(Long movieId, MemberId memberId, MovieCommentRequest request) {
        validateMemberExistsInMovieComment(memberId, movieId);

        MovieCommentId movieCommentId = IdFactory.createMovieCommentId();
        LocalDateTime now = LocalDateTime.now();
        String comment = request.getComment();
        Double score = request.getScore();

        MovieCommentEntity movieCommentEntity = MovieConvertor.toMovieCommentEntity(
                movieId, memberId, comment, score, movieCommentId, now);
        movieCommentLikeCountService.save(
                MovieConvertor.toMovieCommentLikeCountEntity(movieCommentEntity.getMovieCommentId()));
        return MovieConvertor.toMovieCommentResponse(movieCommentRepository.createComment(movieCommentEntity));
    }

    private void validateMemberExistsInMovieComment(MemberId memberId, Long movieId) {
        if (movieDetailReadService.isExistsByMemberIdAndMovieId(memberId, movieId)) {
            throw new MemberExistsInMovieCommentException();
        }
    }

    public void deleteComment(MemberId currentMemberId, MovieCommentId movieCommentId) {
        MovieCommentEntity movieCommentEntity = movieCommentRepository.fetchById(movieCommentId);
        validateMyComment(currentMemberId, movieCommentEntity.getMemberId());
        movieCommentRepository.deleteComment(movieCommentId);
    }

    public void updateComment(MovieCommentId movieCommentId, MemberId currentMemberId,
                              MovieCommentRequest request) {
        MovieCommentEntity movieCommentEntity = movieCommentRepository.fetchById(movieCommentId);
        validateMyComment(currentMemberId, movieCommentEntity.getMemberId());
        movieCommentEntity.updateComment(request, LocalDateTime.now());
    }

    private void validateMyComment(MemberId currentMemberId, MemberId commentMemberId) {
        if (!Objects.equals(currentMemberId, commentMemberId)) {
            throw new MemberNotFoundException();
        }
    }

}
