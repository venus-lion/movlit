package movlit.be.acceptance.movie;

import static movlit.be.acceptance.movie.MovieDetailSteps.상태코드가_200이고_응답_데이터가_존재한다;
import static movlit.be.acceptance.movie.MovieDetailSteps.상태코드가_200이고_응답에_예상된_movieId가_존재한다;
import static movlit.be.acceptance.movie.MovieDetailSteps.영화_상세페이지_내에_있는_장르들을_조회한다;
import static movlit.be.acceptance.movie.MovieDetailSteps.영화_상세페이지_내에_있는_크루들을_조회한다;
import static movlit.be.acceptance.movie.MovieDetailSteps.영화_상세페이지를_조회한다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("영화 상세 페이지 관련 인수테스트")
class MovieDetailAcceptanceTest extends AcceptanceTest {

    @DisplayName("영화 상세 페이지를 가져오는 데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_detail_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieDetail_success", spec);

        // given
        String movieId = String.valueOf(767L);

        // when
        var response = 영화_상세페이지를_조회한다(movieId, spec);

        // then
        상태코드가_200이고_응답에_예상된_movieId가_존재한다(response);
    }

    @DisplayName("영화 상세 페이지 내에 있는 크루들의 데이터를 가져오는 데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_detail_crews_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieDetailCrews_success", spec);

        // given
        String movieId = String.valueOf(767L);

        // when
        var response = 영화_상세페이지_내에_있는_크루들을_조회한다(movieId, spec);

        // then
        상태코드가_200이고_응답_데이터가_존재한다(response);
    }

    @DisplayName("영화 상세 페이지 내에 있는 장르들의 데이터를 가져오는 데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_detail_genres_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieDetailGenres_success", spec);

        // given
        String movieId = String.valueOf(767L);

        // when
        var response = 영화_상세페이지_내에_있는_장르들을_조회한다(movieId, spec);

        // then
        상태코드가_200이고_응답_데이터가_존재한다(response);
    }

}
