package movlit.be.acceptance.movie;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.MovieGenre;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MovieSearchSteps {
    /**
     * 1. 유저_관심_장르와_유사한_영화를_조회한다.
     * 2. 인기순_영화리스트를_조회한다.
     * 3. 장르별_영화리스트를_조회한다.
     * */

    public static ExtractableResponse<Response> 로그인유저_관심장르_영화_리스트를_조회한다(String accessToken, RequestSpecification spec){
        return RestAssured
                .given()
                .param("page", 1)
                .param("pageSize", 10)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/movies/search/interestGenre")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 찜한_영화_크루_유사한_영화_리스트를_조회한다(String accessToken, RequestSpecification spec){
        return RestAssured
                .given()
                .param("page", 1)
                .param("pageSize", 10)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .auth().oauth2(accessToken)
                .when()
                .get("/api/movies/search/lastHeart")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 검색어를_입력하여_영화_리스트를_조회한다(String inputStr, RequestSpecification spec){
        return RestAssured
                .given()
                .param("inputStr", inputStr)
                .param("page", 1)
                .param("pageSize", 20)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .get("/api/movies/search/searchMovie")
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_200이고_응답_데이터가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK),
                () -> assertThat(response).isNotNull()
        );
    }

    public static void 선호장르_하나이상_맞는_응답_데이터가_존재한다(ExtractableResponse<Response> response, List<Long> userInterestGenreIds) {
        Assertions.assertAll(
                () -> {
                    List<Movie> movieList = response.jsonPath().getList("movieList", Movie.class);
                    movieList.forEach(m -> {
                        boolean hasOverlap = m.getMovieGenreList().stream()
                                .map(MovieGenre::getGenreId).anyMatch(userInterestGenreIds::contains);
                        assertThat(hasOverlap)
                                .as("두 리스트 중 하나 이상 겹치는 값이 있어야 합니다.")
                                .isTrue();
                    });
                }
        );
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                   HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    public static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response,
                                                  String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }
}
