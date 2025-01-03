package movlit.be.acceptance.movie_comment;

import static movlit.be.acceptance.movie_comment.MovieCommentSteps.로그인_후_영화_코멘트_목록_조회를_요청한다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.상태코드가_200이다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_목록_조회를_요청한다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_수정을_요청한다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다_2;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다_3;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다_4;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트를_삭제한다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("영화 코멘트 인수 테스트")
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
            상태코드가_200이다(response);
        }

    }

    @Nested
    @DisplayName("영화 코멘트 수정 인수 테스트")
    class UpdateMovieComment {

        String movieCommentId;

        @BeforeEach
        public void before() {
            String accessToken = 회원_원준_액세스토큰;
            String movieId = String.valueOf(767L);
            movieCommentId = 영화_코멘트_작성을_요청한다(accessToken, movieId, spec)
                    .body().jsonPath().getString("commentId");
        }

        @DisplayName("영화 코멘트를 수정하는 데 성공하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_update_movie_comment_then_response_200_and_body() {
            // docs
            api_문서_타이틀("updateMovieComment_success", spec);

            // given
            String accessToken = 회원_원준_액세스토큰;

            // when
            var response = 영화_코멘트_수정을_요청한다(accessToken, movieCommentId, spec);

            // then
            상태코드가_200이다(response);
        }

    }

    @Nested
    @DisplayName("영화 코멘트 조회 인수 테스트")
    class ReadMovieComment {

        String movieId;

        @BeforeEach
        public void before() {
            movieId = String.valueOf(767L);
            영화_코멘트_작성을_요청한다(회원_원준_액세스토큰, movieId, spec);
            영화_코멘트_작성을_요청한다_2(회원_민지_액세스토큰, movieId, spec);
            영화_코멘트_작성을_요청한다_3(회원_윤기_액세스토큰, movieId, spec);
            영화_코멘트_작성을_요청한다_4(회원_지원_액세스토큰, movieId, spec);
        }

        @DisplayName("로그인하지 않은 상태에서, 영화 코멘트 목록을 조회하는 데 성공하면, 상태코드 200을 반환한다.")
        @Test
        void when_read_movie_comment_list_then_response_200_and_body() {
            // docs
            api_문서_타이틀("fetchMovieCommentList_success", spec);

            // given
            // when
            var response = 영화_코멘트_목록_조회를_요청한다(movieId, spec);

            // then
            상태코드가_200이다(response);
        }

        @DisplayName("로그인 한 상태에서, 영화 코멘트 목록을 조회하는 데 성공하면, 상태코드 200을 반환한다.")
        @Test
        void when_read_movie_comment_list_with_session_then_response_200_and_body() {
            // docs
            api_문서_타이틀("fetchMovieCommentList_withSession_success", spec);

            // given
            // when
            var response = 로그인_후_영화_코멘트_목록_조회를_요청한다(회원_원준_액세스토큰, movieId, spec);

            // then
            상태코드가_200이다(response);
        }

    }

    @Nested
    @DisplayName("영화 코멘트 삭제 인수 테스트")
    class DeleteMovieComment {

        String movieCommentId;

        @BeforeEach
        public void before() {
            String accessToken = 회원_원준_액세스토큰;
            String movieId = String.valueOf(767L);
            movieCommentId = 영화_코멘트_작성을_요청한다(accessToken, movieId, spec)
                    .body().jsonPath().getString("commentId");
        }

        @DisplayName("영화 코멘트를 삭제하는 데 성공하면, 상태코드 200을 반환한다.")
        @Test
        void when_delete_movie_comment_then_response_200_and_body() {
            // docs
            api_문서_타이틀("deleteMovieComment_success", spec);

            // given
            String accessToken = 회원_원준_액세스토큰;

            // when
            var response = 영화_코멘트를_삭제한다(accessToken, movieCommentId, spec);

            // then
            상태코드가_200이다(response);
        }

    }


}
