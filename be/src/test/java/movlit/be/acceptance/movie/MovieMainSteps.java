package movlit.be.acceptance.movie;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MovieMainSteps {
    /**
     * 1. 최신순_영화리스트를_조회한다.
     * 2. 인기순_영화리스트를_조회한다.
     * 3. 장르별_영화리스트를_조회한다.
     * + TODO : 현재 접속 사용자 가져와서 사용자의 취향 장르, 키워드, 배우 별 리스트 조회
     * */

    public static ExtractableResponse<Response> 최신순_영화_리스트를_조회한다(int page, int pageSize,
                                                                 RequestSpecification spec){
        return RestAssured
                .given()
                .param("page", page)
                .param("pageSize", pageSize)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .get("/api/movies/main/latest")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 인기순_영화_리스트를_조회한다(int page, int pageSize,
                                                                 RequestSpecification spec){
        return RestAssured
                .given()
                .param("page", page)
                .param("pageSize", pageSize)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .get("/api/movies/main/popular")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 장르별_영화_리스트를_조회한다(Long genreId, int page, int pageSize,
                                                                 RequestSpecification spec){

        return RestAssured
                .given()
                .param("genreId", genreId)
                .param("page", page)
                .param("pageSize", pageSize)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .get("/api/movies/main/genre")
                .then()
                .log().all()
                .extract();

    }

    public static ExtractableResponse<Response> 로그인유저_선호장르_영화_리스트를_조회한다(String accessToken, int page, int pageSize,
                                                                        RequestSpecification spec){
        return RestAssured
                .given()
                .param("page", page)
                .param("pageSize", pageSize)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .auth().oauth2(accessToken)
                .when()
                .post("/api/movies/main/interestGenre")
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

    public static void 상태코드가_200이고_genreId에_맞는_응답_데이터가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK),
                () -> assertThat(response.jsonPath().getString("genreId")).isEqualTo("10"),
                () -> assertThat(response.jsonPath().getString("genreName")).isEqualTo("미스터리"),
                () -> assertThat(response.jsonPath().getList("data")).isNotEmpty()
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
