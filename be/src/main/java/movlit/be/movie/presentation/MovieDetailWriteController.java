package movlit.be.movie.presentation;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.annotation.CurrentMemberId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.application.service.MovieDetailWriteService;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieDetailWriteController {

    private final MovieDetailWriteService movieDetailWriteService;

    @PostMapping("/api/movies/{movieId}/comments")
    public ResponseEntity<MovieCommentResponse> createComment(@PathVariable Long movieId,
                                                              @RequestBody MovieCommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자 정보가 없는 경우 (로그인하지 않은 경우)
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized 응답
//        }

        // 인증된 사용자 정보에서 사용자 이름(username) 또는 ID 가져오기
        String username = authentication.getName(); // 일반적으로 username을 사용
        log.info("username={}", username);

        MemberId memberId = null;
        var data = MovieConvertor.toMovieDetailCommentData(movieId, memberId, request);
        var response = movieDetailWriteService.createComment(data);
        return ResponseEntity.ok(response);
    }

}
