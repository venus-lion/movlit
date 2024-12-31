package movlit.be.movie.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.common.annotation.CurrentMemberId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.application.service.MovieDetailWriteService;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieDetailWriteController {

    private final MovieDetailWriteService movieDetailWriteService;

    @PostMapping("/api/movies/{movieId}/comments")
    public ResponseEntity<MovieCommentResponse> createComment(@PathVariable Long movieId,
                                                              @CurrentMemberId MemberId memberId,
                                                              @RequestBody MovieCommentRequest request) {

        var data = MovieConvertor.toMovieDetailCommentData(movieId, memberId, request);
        var response = movieDetailWriteService.createComment(data);
        return ResponseEntity.ok(response);
    }

}
