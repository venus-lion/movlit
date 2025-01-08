package movlit.be.movie_comment_heart.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie_comment_heart.application.service.MovieCommentLikeService;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MovieCommentLikeController {

    private final MovieCommentLikeService movieCommentLikeService;

    @PostMapping("/api/movies/comments/{commentId}/likes")
    public ResponseEntity<MovieCommentLikeResponse> like(@PathVariable MovieCommentId commentId,
                                                         @AuthenticationPrincipal MyMemberDetails details) {
        var response = movieCommentLikeService.like(details.getMemberId(), commentId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/api/movies/comments/{commentId}/likes")
    public ResponseEntity<Void> unlike(@PathVariable MovieCommentId commentId,
                                       @AuthenticationPrincipal MyMemberDetails details) {
        movieCommentLikeService.unlike(details.getMemberId(), commentId);
        return ResponseEntity.ok().build();
    }

}
