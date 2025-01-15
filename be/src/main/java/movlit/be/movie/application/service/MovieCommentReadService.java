package movlit.be.movie.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import movlit.be.movie.presentation.dto.response.MovieMyCommentReadResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieCommentReadService {

    private final MovieCommentRepository movieCommentRepository;

    public Slice<MovieCommentReadResponse> fetchMovieComments(Long movieId, Pageable pageable) {
        return movieCommentRepository.fetchComments(movieId, pageable);
    }

    public Slice<MovieCommentReadResponse> fetchMovieComments(Long movieId, MemberId memberId, Pageable pageable) {
        return movieCommentRepository.fetchComments(movieId, memberId, pageable);
    }

    public MovieMyCommentReadResponse fetchMyMovieComment(Long movieId, MemberId currentMemberId) {
        return movieCommentRepository.fetchMyComment(movieId, currentMemberId);
    }

    public boolean isExistsByMemberIdAndMovieId(MemberId memberId, Long movieId) {
        return movieCommentRepository.existsByMemberIdAndMovieId(memberId, movieId);
    }

}
