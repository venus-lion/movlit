package movlit.be.movie_comment_heart.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieCommentLikeAlreadyExistsException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart.domain.repository.MovieCommentLikeRepository;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.application.service.MovieCommentLikeCountReadService;
import movlit.be.movie_comment_heart_count.application.service.MovieCommentLikeCountWriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieCommentLikeWriteService {

    private final MovieCommentLikeRepository movieCommentLikeRepository;
    private final MovieCommentLikeReadService movieCommentLikeReadService;
    private final MovieCommentLikeCountWriteService movieCommentLikeCountWriteService;
    private final MovieCommentLikeCountReadService movieCommentLikeCountReadService;
    private final MemberReadService memberReadService;

    public MovieCommentLikeResponse like(MemberId memberId, MovieCommentId movieCommentId) {
        memberReadService.validateMemberIdExists(memberId);
        validateMovieCommentLikeExists(movieCommentId, memberId);
        MovieCommentLikeEntity movieCommentLikeEntity = movieCommentLikeRepository.like(
                MovieConvertor.makeMovieCommentLikeEntity(memberId, movieCommentId));
        movieCommentLikeCountWriteService.incrementMovieCommentLikeCount(movieCommentLikeEntity.getMovieCommentId());
        return movieCommentLikeCountReadService.fetchMovieCommentLikeResponse(
                movieCommentLikeEntity.getMovieCommentLikeId());
    }

    public void unlike(MemberId memberId, MovieCommentId commentId) {
        memberReadService.validateMemberIdExists(memberId);
        validateMovieCommentLikeNotExist(commentId, memberId);
        movieCommentLikeRepository.deleteByMovieCommentId(commentId);
        movieCommentLikeCountWriteService.decrementMovieCommentLikeCount(commentId);
    }

    public void validateMovieCommentLikeExists(MovieCommentId movieCommentId, MemberId memberId) {
        if (movieCommentLikeReadService.existsByMovieCommentIdAndMemberId(movieCommentId, memberId)) {
            throw new MovieCommentLikeAlreadyExistsException();
        }
    }

    public void validateMovieCommentLikeNotExist(MovieCommentId movieCommentId, MemberId memberId) {
        if (!movieCommentLikeReadService.existsByMovieCommentIdAndMemberId(movieCommentId, memberId)) {
            throw new MovieCommentLikeAlreadyExistsException();
        }
    }

}
