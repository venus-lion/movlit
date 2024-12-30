package movlit.be.movie.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.movie.application.service.MovieMainService;
import movlit.be.movie.domain.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies/main")
@RequiredArgsConstructor
@Slf4j
public class MovieMainController {

    private final MovieMainService movieMainService;

    // 개봉순
    @GetMapping("/latest")
    public ResponseEntity<List<Movie>> getMovieLatest(@RequestParam(required = false, defaultValue = "1") String page) {
        List<Movie> movieList = movieMainService.getMovieLatest(page);

        return ResponseEntity.ok(movieList);
    }

    // 인기순
    @GetMapping("/popular")
    public ResponseEntity<List<Movie>> getMoviePopular(@RequestParam(required = false, defaultValue = "1") String page) {
        List<Movie> movieList = movieMainService.getMoviePopular(page);

        return ResponseEntity.ok(movieList);
    }

    // 장르별 (Map<List<Movie>>)
}
