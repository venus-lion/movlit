package movlit.be.acceptance.movie;

import static movlit.be.acceptance.movie.MovieSearchSteps.상태코드가_200이고_응답_데이터가_존재한다;
import static movlit.be.acceptance.movie.MovieSearchSteps.로그인유저_관심장르_영화_리스트를_조회한다;
import static movlit.be.acceptance.movie.MovieSearchSteps.선호장르_하나이상_맞는_응답_데이터가_존재한다;

import java.util.List;
import movlit.be.acceptance.AcceptanceTest;
import movlit.be.acceptance.util.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("영화 메인 페이지 추천 검색 관련 인수테스트")
class MovieSearchAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("로그인 유저 기반 추천 영화 리스트 search 테스트")
    class LoginUserInterestGenreMovieList{
        String accessToken;

        @BeforeEach
        public void before(){
            accessToken = 회원_윤기_액세스토큰;
        }

        @DisplayName("사용자 관심 장르와 유사한 영화를 가져오는데 성공하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_user_login_has_Interest_Genre_then_response_200_and_body() {
            // docs
            api_문서_타이틀("userLoginHasInterestGenre_success", spec);

            // given
            List<Long> genreList = MemberFixture.회원_윤기.getGenreIds();

            // when
            var response = 로그인유저_관심장르_영화_리스트를_조회한다(accessToken, spec);

            // then
            상태코드가_200이고_응답_데이터가_존재한다(response);
            선호장르_하나이상_맞는_응답_데이터가_존재한다(response, genreList);
        }
    }
}
