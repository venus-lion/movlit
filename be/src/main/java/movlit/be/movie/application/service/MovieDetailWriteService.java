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
import movlit.be.movie.presentation.dto.request.MovieCommentData;
import movlit.be.movie.presentation.dto.request.MovieCommentDataForDelete;
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
    private final MovieCommentLikeCountService movieCommentLikeCountService;

    public MovieCommentResponse createComment(MovieCommentData data) {
        validateMemberExistsInMovieComment(data);
        MovieCommentId movieCommentId = IdFactory.createMovieCommentId();
        LocalDateTime now = LocalDateTime.now();
        MovieCommentEntity movieCommentEntity = MovieConvertor.toMovieCommentEntity(data, movieCommentId, now);
        Long movieCommentLikeCount = movieCommentLikeCountService.fetchMovieCommentLikeCountByMovieIdAndMovieCommentId(
                movieCommentEntity.getMovieCommentId());
        movieCommentLikeCountService.save(
                MovieConvertor.toMovieCommentLikeCountEntity(movieCommentEntity, movieCommentLikeCount));
        return MovieConvertor.toMovieCommentResponse(movieCommentRepository.createComment(movieCommentEntity));
    }

    @Transactional(readOnly = true)
    public void validateMemberExistsInMovieComment(MovieCommentData data) {
        if (movieCommentRepository.fetchByMemberIdAndMovieId(data.memberId(), data.movieId()).isPresent()) {
            throw new MemberExistsInMovieCommentException();
        }
    }

    public void deleteComment(MovieCommentDataForDelete data) {
        MemberId currentMemberId = data.currentMemberId();
        MovieCommentId movieCommentId = data.movieCommentId();
        MovieCommentEntity movieCommentEntity = movieCommentRepository.fetchById(movieCommentId);
        validateMyComment(currentMemberId, movieCommentEntity);
        movieCommentRepository.deleteComment(movieCommentId);
    }

    public void updateComment(MovieCommentId movieCommentId, MemberId currentMemberId,
                              MovieCommentRequest request) {
        MovieCommentEntity movieCommentEntity = movieCommentRepository.fetchById(movieCommentId);
        validateMyComment(currentMemberId, movieCommentEntity);
        movieCommentEntity.updateComment(request, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public void validateMyComment(MemberId currentMemberId, MovieCommentEntity movieCommentEntity) {
        if (!Objects.equals(currentMemberId, movieCommentEntity.getMemberId())) {
            throw new MemberNotFoundException();
        }
    }

}
