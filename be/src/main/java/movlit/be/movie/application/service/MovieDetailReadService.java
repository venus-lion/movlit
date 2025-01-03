package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.domain.repository.MovieCrewRepository;
import movlit.be.movie.domain.repository.MovieDetailRepository;
import movlit.be.movie.domain.repository.MovieGenreRepository;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieDetailReadService {

    private final MovieDetailRepository movieDetailRepository;
    private final MovieCrewRepository movieCrewRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieCommentRepository movieCommentRepository;

    public MovieDetailResponse fetchMovieDetail(Long movieId) {
        return movieDetailRepository.fetchMovieDetailByMovieId(movieId);
    }

    public List<MovieDetailCrewResponse> fetchMovieDetailCrews(Long movieId) {
        return movieCrewRepository.fetchMovieDetailCrewsByMovieId(movieId);
    }

    public List<MovieDetailGenreResponse> fetchMovieDetailGenres(Long movieId) {
        return movieGenreRepository.fetchMovieDetailGenresByMovieId(movieId);
    }

    public Slice<MovieCommentReadResponse> fetchMovieComments(Long movieId) {
        return movieCommentRepository.fetchComments(movieId);
    }

    public Slice<MovieCommentReadResponse> fetchMovieComments(Long movieId, MemberId memberId) {
        return movieCommentRepository.fetchComments(movieId, memberId);
    }

}
