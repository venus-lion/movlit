package movlit.be.acceptance.movie;

import static movlit.be.acceptance.movie.MovieMainSteps.상태코드가_200이고_genreId에_맞는_응답_데이터가_존재한다;
import static movlit.be.acceptance.movie.MovieMainSteps.상태코드가_200이고_응답_데이터가_존재한다;
import static movlit.be.acceptance.movie.MovieMainSteps.인기순_영화_리스트를_조회한다;
import static movlit.be.acceptance.movie.MovieMainSteps.장르별_영화_리스트를_조회한다;
import static movlit.be.acceptance.movie.MovieMainSteps.최신순_영화_리스트를_조회한다;

import movlit.be.acceptance.AcceptanceTest;
import movlit.be.common.util.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("영화 메인 페이지 관련 인수테스트")
class MovieMainAcceptanceTest extends AcceptanceTest {

    @DisplayName("최신순 영화 리스트를 가져오는데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_list_by_latest_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieListByLatest_success", spec);

        // given
        int page = 1, pageSize = 10;

        // when
        var response = 최신순_영화_리스트를_조회한다(page, pageSize, spec);

        // then
        상태코드가_200이고_응답_데이터가_존재한다(response);
    }

    @DisplayName("인기순 영화 리스트를 가져오는데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_list_by_popular_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieListByPopular_success", spec);

        // given
        int page = 1, pageSize = 10;

        // when
        var response = 인기순_영화_리스트를_조회한다(page, pageSize, spec);

        // then
        상태코드가_200이고_응답_데이터가_존재한다(response);
    }

    @DisplayName("장르별 영화 리스트를 가져오는데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_list_by_genre_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieListByGenre_success", spec);

        // given
        Long genreId = Genre.ACTION.getId();
        int page = 1, pageSize = 10;

        // when
        var response = 장르별_영화_리스트를_조회한다(genreId, page, pageSize, spec);

        // then
        상태코드가_200이고_genreId에_맞는_응답_데이터가_존재한다(response, genreId);
    }

    @Nested
    @DisplayName("로그인 유저 추천 영화 리스트 search 테스트")
    class LoginUserInterestGenreMovieList{
        String accessToken;

        @BeforeEach
        public void before(){
            accessToken = 회원_윤기_액세스토큰;
        }

        @DisplayName("로그인 상태가 맞고 선호 장르가 존재하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_user_login_has_Interest_Genre_then_response_200_and_body() {
            // docs

        }
    }
}
