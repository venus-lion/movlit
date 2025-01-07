package movlit.be.movie.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.service.MovieMainService;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies/search")
@RequiredArgsConstructor
@Slf4j
public class MovieMainSearchController {

    private final MovieMainService movieMainService;

    /**
     * 사용자 별 취향 (장르) 가져오기
     * */
    @GetMapping("/interestGenre")
    public ResponseEntity<MovieListResponseDto> getMovieUserInterestByGenre(
            @AuthenticationPrincipal MyMemberDetails details,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        MemberId currentMemberId = details.getMemberId();
//        MemberId currentMemberId = new MemberId("3e5bd1120000003fa8ece4dd");
        MovieListResponseDto response = movieMainService.getMovieUserInterestByGenre(currentMemberId, page, pageSize);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 유저의 최근 찜 영화 기반으로 유사한 영화 리스트 가져오기
     * */
    @GetMapping("/lastHeart")
    public ResponseEntity<MovieListResponseDto> getMovieByUserRecentHeart(
//            @AuthenticationPrincipal MyMemberDetails details,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
//        MemberId currentMemberId = details.getMemberId();
        MemberId currentMemberId = new MemberId("3c674f030000002cf9970db2");
        MovieListResponseDto response = movieMainService.getMovieByUserRecentHeart(currentMemberId, page, pageSize);

        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 유저의 최근 평가 영화 기반으로 유사한 영화 리스트 가져오기
     * */
}
