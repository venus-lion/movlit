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

    /**
     * 최신 영화 내림차순 리스트
     * RequestParam - {조회 page 번호, 조회 page size 갯수}
     */
    @GetMapping("/latest")
    public ResponseEntity<List<Movie>> getMovieLatest(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        List<Movie> movieList = movieMainService.getMovieLatest(page, pageSize);

        return ResponseEntity.ok(movieList);
    }

    /**
     * 인기순 내림차순 리스트
     * RequestParam - {조회 page 번호, 조회 page size 갯수}
     */
    @GetMapping("/popular")
    public ResponseEntity<List<Movie>> getMoviePopular(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        List<Movie> movieList = movieMainService.getMoviePopular(page, pageSize);

        return ResponseEntity.ok(movieList);
    }

    /**
     * 장르별 개봉날짜 내림차순 리스트
     * RequestParam - {조회 장르 ID, 조회 page 번호, 조회 page size 갯수}
     * */
    @GetMapping("/genre")
    public ResponseEntity<List<Movie>> getMovieGroupbyGenre(
            @RequestParam(required = true) Long genreId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        List<Movie> movieList = movieMainService.getMovieGroupbyGenre(genreId, page, pageSize);
        return ResponseEntity.ok(movieList);
    }

    /**
    * TODO : 사용자 별 취향 (키워드, 장르, 배우) 가져오기
    *
    * */


    /**
     * TODO : 사용자 로그인 유무에 따른 처리를 Spring에서 할지? 한다면 여기에 구현
     *
     * */


}
