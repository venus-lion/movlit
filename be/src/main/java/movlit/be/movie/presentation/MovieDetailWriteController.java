package movlit.be.movie.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.application.service.MovieDetailWriteService;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieDetailWriteController {

    private final MovieDetailWriteService movieDetailWriteService;

    @PostMapping("/api/movies/{movieId}/comments")
    public ResponseEntity<MovieCommentResponse> createComment(@PathVariable Long movieId,
                                                              @AuthenticationPrincipal MyMemberDetails details,
                                                              @RequestBody MovieCommentRequest request) {
        MemberId memberId = details.getMemberId();
        var response = movieDetailWriteService.createComment(movieId, memberId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/movies/comments/{movieCommentId}")
    public ResponseEntity<Void> updateComment(@PathVariable MovieCommentId movieCommentId,
                                              @AuthenticationPrincipal MyMemberDetails details,
                                              @RequestBody MovieCommentRequest request) {
        MemberId currentMemberId = details.getMemberId();
        movieDetailWriteService.updateComment(movieCommentId, currentMemberId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/movies/comments/{movieCommentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable MovieCommentId movieCommentId,
                                              @AuthenticationPrincipal MyMemberDetails details) {
        movieDetailWriteService.deleteComment(details.getMemberId(), movieCommentId);
        return ResponseEntity.ok().build();
    }

}
