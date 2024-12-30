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

public class MovieDetailSteps {

    public static ExtractableResponse<Response> 영화_상세페이지를_조회한다(String movieId,
                                                               RequestSpecification spec) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .log().all()
                .when()
                .get("/api/movies/" + movieId + "/detail")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 영화_상세페이지_내에_있는_크루들을_조회한다(String movieId,
                                                                         RequestSpecification spec) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .log().all()
                .when()
                .get("/api/movies/" + movieId + "/crews")
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_200이고_응답에_예상된_movieId가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK)
        );
    }

    public static void 상태코드가_200이고_응답_데이터가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK),
                () -> assertThat(response).isNotNull());
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    public static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response, String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }

}
