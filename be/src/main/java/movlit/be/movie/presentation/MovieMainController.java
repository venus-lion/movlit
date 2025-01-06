package movlit.be.movie.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.service.MovieMainService;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<MovieListResponseDto> getMovieLatest(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        MovieListResponseDto result = movieMainService.getMovieLatest(page, pageSize);

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

        MovieListResponseDto result = movieMainService.getMoviePopular(page, pageSize);

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

        MovieListByGenreResponseDto reponseDto = movieMainService.getMovieGroupbyGenre(genreId, page, pageSize);
        return ResponseEntity.ok().body(reponseDto);
    }

    /**
     * 사용자 별 취향 (장르) 가져오기
     * TODO : 키워드, 배우 별도 고려
     * */
    @GetMapping("/interestGenre")
    public ResponseEntity<MovieListResponseDto> getMovieUserInterestByGenre(
            @AuthenticationPrincipal MyMemberDetails details,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

//        MemberId currentMemberId = details.getMemberId();
        MemberId currentMemberId = new MemberId("2a8276a9000000e20097ec8e");
        MovieListResponseDto response = movieMainService.getMovieUserInterestByGenre(currentMemberId, page, pageSize);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 로그인 유저의 최근 찜 목록 기반으로 유사한 영화 리스트 가져오기
     * */
    @GetMapping("/lastHeart")
    public ResponseEntity<MovieListResponseDto> getMovieByUserRecentHeart(
            @AuthenticationPrincipal MyMemberDetails details,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
//        MemberId currentMemberId = details.getMemberId();
        MemberId currentMemberId = new MemberId("2a8276a9000000e20097ec8e");
        MovieListResponseDto response = movieMainService.getMovieByUserRecentHeart(currentMemberId);

        return ResponseEntity.ok(null);
    }
}
