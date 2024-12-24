package movlit.be.data_collection;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.Movie;
import movlit.be.MovieCrew;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class MovieCollectController {

    private final MovieCollectService movieCollectService;

    @GetMapping("/discover")
    public void getDiscoverMovie() {
        int MAX_PAGE = 100; // 변경 가능
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
    }

    @GetMapping("/keywords")
    public void getMovieTagList(){
        List<Movie> movieList = movieCollectService.getAllMovieList();
        int cnt = 0;
        for(Movie movie : movieList){

            movieCollectService.getMovieTagList(movie);
            try{
                if (cnt % 40 == 0) {
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e){
                e.printStackTrace();
            }
            log.info("cnt={}", cnt);
            log.info("id={}", movie.getId());
            cnt ++;
        }

    }

    @GetMapping("/discover/crew")
    public void getDiscoverMovieCrew() {
        movieCollectService.getMovieCrewList();
    }

}
