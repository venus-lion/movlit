package movlit.be.acceptance.movie_comment_like;

import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다;
import static movlit.be.acceptance.movie_comment_like.MovieCommentLikeSteps.상태코드가_200이다;
import static movlit.be.acceptance.movie_comment_like.MovieCommentLikeSteps.영화_코멘트_좋아요를_취소한다;
import static movlit.be.acceptance.movie_comment_like.MovieCommentLikeSteps.영화_코멘트에_좋아요를_누른다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("영화 댓글 좋아요 인수 테스트")
public class MovieCommentLikeAcceptanceTest extends AcceptanceTest {

    String movieId;
    String movieCommentId;

    @BeforeEach
    public void before() {
        movieId = String.valueOf(767L);
        String accessToken = 회원_원준_액세스토큰;
        movieCommentId = 영화_코멘트_작성을_요청한다(accessToken, movieId, spec).jsonPath().getString("commentId");
    }

    @DisplayName("영화 댓글 좋아요 버튼을 누르고 반영이 성공적으로 되면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_like_movie_comment_then_response_200_and_body() {
        // docs
        api_문서_타이틀("movie_comment_like_increment_success", spec);

        // when
        var response = 영화_코멘트에_좋아요를_누른다(movieCommentId, 회원_민지_액세스토큰, spec);

        // then
        상태코드가_200이다(response);
    }

    @DisplayName("영화 댓글 좋아요의 취소가 성공적으로 되면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_unlike_movie_comment_then_response_200_and_body() {
        // docs
        api_문서_타이틀("movie_comment_unlike_decrement_success", spec);

        // when
//        영화_코멘트에_좋아요를_누른다(movieCommentId, 회원_지원_액세스토큰, spec);
        영화_코멘트에_좋아요를_누른다(movieCommentId, 회원_민지_액세스토큰, spec);
//        영화_코멘트에_좋아요를_누른다(movieCommentId, 회원_윤기_액세스토큰, spec);

        var response = 영화_코멘트_좋아요를_취소한다(movieCommentId, 회원_민지_액세스토큰, spec);

        // then
        상태코드가_200이다(response);
    }

}
