package movlit.be.movie_heart.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.movie_heart.application.service.MovieHeartService;
import movlit.be.movie_heart.presentation.dto.response.MovieHeartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MovieHeartController {

    private final MovieHeartService movieHeartService;

    @PostMapping("/api/movies/{movieId}/hearts")
    public ResponseEntity<MovieHeartResponse> heart(@PathVariable Long movieId,
                                                    @AuthenticationPrincipal MyMemberDetails details) {
        var response = movieHeartService.heart(movieId, details.getMemberId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/api/movies/{movieId}/hearts")
    public ResponseEntity<Void> unHeart(@PathVariable Long movieId,
                                                    @AuthenticationPrincipal MyMemberDetails details) {
        movieHeartService.unHeart(movieId, details.getMemberId());
        return ResponseEntity.ok().build();
    }

}
