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
import movlit.be.movie_comment_heart_count.application.service.MovieCommentLikeCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieCommentLikeService {

    private final MovieCommentLikeRepository movieCommentLikeRepository;
    private final MovieCommentLikeCountService movieCommentLikeCountService;
    private final MemberReadService memberReadService;

    @Transactional
    public MovieCommentLikeResponse like(MemberId memberId, MovieCommentId movieCommentId) {
        memberReadService.validateMemberIdExists(memberId);
        validateMovieCommentLikeExists(movieCommentId, memberId);
        MovieCommentLikeEntity movieCommentLikeEntity = movieCommentLikeRepository.like(
                MovieConvertor.toMovieCommentLikeEntity(memberId, movieCommentId));
        movieCommentLikeCountService.incrementMovieCommentLikeCount(movieCommentLikeEntity.getMovieCommentId());
        Long movieCommentLikeCount = movieCommentLikeCountService.fetchMovieCommentLikeCountByMovieIdAndMovieCommentId(
                movieCommentLikeEntity.getMovieCommentId());
        return MovieConvertor.toMovieCommentLikeResponse(movieCommentLikeEntity, movieCommentLikeCount);
    }

    @Transactional
    public void unlike(MemberId memberId, MovieCommentId commentId) {
        memberReadService.validateMemberIdExists(memberId);
        validateMovieCommentLikeNotExist(commentId, memberId);
        movieCommentLikeRepository.deleteByMovieCommentId(commentId);
        movieCommentLikeCountService.decrementMovieCommentLikeCount(commentId);
    }

    @Transactional(readOnly = true)
    public void validateMovieCommentLikeExists(MovieCommentId movieCommentId, MemberId memberId) {
        if (movieCommentLikeRepository.existsByMovieCommentIdAndMemberId(movieCommentId, memberId)) {
            throw new MovieCommentLikeAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public void validateMovieCommentLikeNotExist(MovieCommentId movieCommentId, MemberId memberId) {
        if (!movieCommentLikeRepository.existsByMovieCommentIdAndMemberId(movieCommentId, memberId)) {
            throw new MovieCommentLikeAlreadyExistsException();
        }
    }

}
