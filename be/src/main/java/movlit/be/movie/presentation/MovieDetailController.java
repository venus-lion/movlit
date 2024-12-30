package movlit.be.movie.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.annotation.CurrentMemberId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.service.MovieDetailReadService;
import movlit.be.movie.presentation.dto.MovieDetailCrewResponse;
import movlit.be.movie.presentation.dto.MovieDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieDetailController {

    private final MovieDetailReadService movieDetailReadService;

    @GetMapping("/api/movies/{movieId}/detail")
    public ResponseEntity<MovieDetailResponse> fetchMovieDetail(@PathVariable Long movieId, @CurrentMemberId MemberId memberId) {
        var response = movieDetailReadService.fetchMovieDetail(movieId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/movies/{movieId}/crews")
    public ResponseEntity<List<MovieDetailCrewResponse>> fetchMovieDetailCrews(@PathVariable Long movieId, @CurrentMemberId MemberId memberId) {
        var response = movieDetailReadService.fetchMovieDetailCrews(movieId);
        return ResponseEntity.ok(response);
    }

}
