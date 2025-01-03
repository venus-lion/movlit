package movlit.be.movie.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.service.MovieDetailReadService;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;
import movlit.be.movie.presentation.dto.response.MovieDetailResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieDetailReadController {

    private final MovieDetailReadService movieDetailReadService;

    @GetMapping("/api/movies/{movieId}/detail")
    public ResponseEntity<MovieDetailResponse> fetchMovieDetail(@PathVariable Long movieId) {
        var response = movieDetailReadService.fetchMovieDetail(movieId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/movies/{movieId}/crews")
    public ResponseEntity<List<MovieDetailCrewResponse>> fetchMovieDetailCrews(@PathVariable Long movieId) {
        var response = movieDetailReadService.fetchMovieDetailCrews(movieId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/movies/{movieId}/genres")
    public ResponseEntity<List<MovieDetailGenreResponse>> fetchMovieDetailGenres(@PathVariable Long movieId) {
        var response = movieDetailReadService.fetchMovieDetailGenres(movieId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/movies/{movieId}/comments")
    public ResponseEntity<Slice<MovieCommentReadResponse>> fetchMovieComments(
            @PathVariable Long movieId,
            @AuthenticationPrincipal
            MyMemberDetails details,
            @PageableDefault(size = 4, sort = "regDt", direction = Direction.DESC)
            Pageable pageable
    ) {
        if (details == null) {
            var response = movieDetailReadService.fetchMovieComments(movieId, pageable); // TODO: data 사용
            return ResponseEntity.ok(response);
        }

        MemberId currentMemberId = details.getMemberId();
        var response = movieDetailReadService.fetchMovieComments(movieId, currentMemberId, pageable); // TODO: data 사용
        return ResponseEntity.ok(response);
    }

}
