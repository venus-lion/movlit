package movlit.be.movie.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.movie.application.service.MovieDetailSearchService;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MovieDetailSearchController {

    private final MovieDetailSearchService movieDetailSearchService;

    @GetMapping("/api/movies/{movieId}/detail/related")
    public ResponseEntity<MovieListResponseDto> fetchMovieDetailRelated(
            @PathVariable Long movieId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        MovieListResponseDto response = movieDetailSearchService.fetchMovieDetailRelated(page,
                pageSize, movieId);
        return ResponseEntity.ok(response);
    }

}
