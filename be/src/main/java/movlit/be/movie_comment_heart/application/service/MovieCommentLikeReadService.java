package movlit.be.movie_comment_heart.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart.domain.repository.MovieCommentLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieCommentLikeReadService {

    private final MovieCommentLikeRepository movieCommentLikeRepository;

    public boolean existsByMovieCommentIdAndMemberId(MovieCommentId movieCommentId, MemberId memberId) {
        return movieCommentLikeRepository.existsByMovieCommentIdAndMemberId(movieCommentId, memberId);
    }

}
