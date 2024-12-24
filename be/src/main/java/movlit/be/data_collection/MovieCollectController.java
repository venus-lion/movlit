package movlit.be.data_collection;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MovieCollectController {

    private final MovieCollectService movieCollectService;

    @GetMapping("/discover")
    public ResponseEntity<List<List<Movie>>> getDiscoverMovie() {
        int MAX_PAGE = 100;
        List<List<Movie>> resultList = new ArrayList<>();

        for (int i = 1; i <= MAX_PAGE; i++) {
            try {
                List<Movie> movieList = movieCollectService.getMovieDiscoverView(Integer.toString(i));
                if (movieList.isEmpty()) {
                    break;
                }

                resultList.add(movieList);
                if (i % 2 == 0) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok(resultList);
    }

}
