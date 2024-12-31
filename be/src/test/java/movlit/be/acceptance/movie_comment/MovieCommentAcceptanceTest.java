package movlit.be.acceptance.movie_comment;

import static movlit.be.acceptance.movie_comment.MovieCommentSteps.상태코드가_200이고_응답에_movieCommentId가_존재한다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MovieCommentAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("영화 코멘트 등록 인수 테스트")
    class CreateMovieComment {

        String movieId;

        @BeforeEach
        public void before() {
            movieId = String.valueOf(767L);
        }

        @DisplayName("영화 코멘트를 작성하는 데 성공하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_create_movie_comment_then_response_200_and_body() {
            // docs
            api_문서_타이틀("createMovieComment_success", spec);

            // given
            String accessToken = 회원_원준_액세스토큰;

            // when
            var response = 영화_코멘트_작성을_요청한다(accessToken, movieId, spec);

            // then
            상태코드가_200이고_응답에_movieCommentId가_존재한다(response);
        }

    }


}
