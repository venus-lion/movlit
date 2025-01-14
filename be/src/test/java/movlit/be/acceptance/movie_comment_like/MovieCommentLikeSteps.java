package movlit.be.acceptance.movie_comment_like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class MovieCommentLikeSteps {

    public static ExtractableResponse<Response> 영화_코멘트에_좋아요를_누른다(
            String commentId,
            String accessToken,
            RequestSpecification spec
    ) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .spec(spec)
                .auth().oauth2(accessToken)
                .log().all()
                .when()
                .post("/api/movies/comments/{commentId}/likes", commentId)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 영화_코멘트_좋아요를_취소한다(
            String commentId,
            String accessToken,
            RequestSpecification spec
    ) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .spec(spec)
                .auth().oauth2(accessToken)
                .log().all()
                .when()
                .delete("/api/movies/comments/{commentId}/likes", commentId)
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_200이다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK));
    }

    public static void 상태코드가_404이고_오류코드는_m105이다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.NOT_FOUND),
                () -> 오류코드를_검증한다(response, "m105"));
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    public static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response, String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }

}
