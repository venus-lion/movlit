package movlit.be.movie.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.movie.application.service.MovieReadService;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies/main")
@RequiredArgsConstructor
@Slf4j
public class MovieReadController {

    private final MovieReadService movieReadService;

    /**
     * 최신 영화 내림차순 리스트
     * RequestParam - {조회 page 번호, 조회 page size 갯수}
     */
    @GetMapping("/latest")
    public ResponseEntity<MovieListResponseDto> getMovieLatest(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        MovieListResponseDto result = movieReadService.getMovieLatest(page, pageSize);

        return ResponseEntity.ok().body(result);
    }

    /**
     * 인기순 내림차순 리스트
     * RequestParam - {조회 page 번호, 조회 page size 갯수}
     */
    @GetMapping("/popular")
    public ResponseEntity<MovieListResponseDto> getMoviePopular(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        MovieListResponseDto result = movieReadService.getMoviePopular(page, pageSize);

        return ResponseEntity.ok().body(result);
    }

    /**
     * 장르별 개봉날짜 내림차순 리스트
     * RequestParam - {조회 장르 ID, 조회 page 번호, 조회 page size 갯수}
     * TODO : RequestDto 만들기
     * */
    @GetMapping("/genre")
    public ResponseEntity<MovieListByGenreResponseDto> getMovieGroupbyGenre(
            @RequestParam(required = true) Long genreId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        MovieListByGenreResponseDto reponseDto = movieReadService.getMovieGroupbyGenre(genreId, page, pageSize);
        return ResponseEntity.ok().body(reponseDto);
    }
}
