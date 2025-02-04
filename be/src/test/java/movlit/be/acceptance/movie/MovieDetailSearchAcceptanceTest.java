package movlit.be.acceptance.movie;

import static movlit.be.acceptance.movie.MovieDetailSearchSteps.상태코드가_200이고_응답_데이터가_존재한다;
import static movlit.be.acceptance.movie.MovieDetailSearchSteps.영화_페이지의_장르와_연관된_영화_리스트를_조회한다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MovieDetailSearchAcceptanceTest extends AcceptanceTest {

    @DisplayName("영화 장르와 유사한 영화들을 가져오는 데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_movie_genres_with_movie_detail_success_then_response_200_and_body() {
        // docs
        api_문서_타이틀("fetchMovieGenresWithMovieDetail_success", spec);

        // given
        String movieId = String.valueOf(767L);

        // when
        var response = 영화_페이지의_장르와_연관된_영화_리스트를_조회한다(movieId, spec);

        // then
        상태코드가_200이고_응답_데이터가_존재한다(response);
    }

}
