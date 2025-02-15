package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberExistsInMovieCommentException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.domain.repository.MovieCrewRepository;
import movlit.be.movie.domain.repository.MovieDetailRepository;
import movlit.be.movie.domain.repository.MovieGenreRepository;
import movlit.be.movie.presentation.dto.request.MovieCommentData;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailResponse;
import movlit.be.movie.presentation.dto.response.MovieMyCommentReadResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieDetailReadService {

    private final MovieDetailRepository movieDetailRepository;
    private final MovieCrewReadService movieCrewReadService;
    private final MovieGenreReadService movieGenreReadService;
    private final MovieCommentReadService movieCommentReadService;

    public MovieDetailResponse fetchMovieDetail(Long movieId) {
        return movieDetailRepository.fetchMovieDetailByMovieId(movieId);
    }

    public MovieDetailResponse fetchMovieDetailWithMember(Long movieId, MemberId memberId) {
        return movieDetailRepository.fetchMovieDetailByMovieIdAndMemberId(movieId, memberId);
    }

    public List<MovieDetailCrewResponse> fetchMovieDetailCrews(Long movieId) {
        return movieCrewReadService.fetchMovieDetailCrews(movieId);
    }

    public List<MovieDetailGenreResponse> fetchMovieDetailGenres(Long movieId) {
        return movieGenreReadService.fetchMovieDetailGenres(movieId);
    }

    public Slice<MovieCommentReadResponse> fetchMovieComments(Long movieId, Pageable pageable) {
        return movieCommentReadService.fetchMovieComments(movieId, pageable);
    }

    public Slice<MovieCommentReadResponse> fetchMovieComments(Long movieId, MemberId memberId, Pageable pageable) {
        return movieCommentReadService.fetchMovieComments(movieId, memberId, pageable);
    }

    public MovieMyCommentReadResponse fetchMyMovieComment(Long movieId, MemberId currentMemberId) {
        return movieCommentReadService.fetchMyMovieComment(movieId, currentMemberId);
    }

    public boolean isExistsByMemberIdAndMovieId(MemberId memberId, Long movieId) {
        return movieCommentReadService.isExistsByMemberIdAndMovieId(memberId, movieId);
    }

}
