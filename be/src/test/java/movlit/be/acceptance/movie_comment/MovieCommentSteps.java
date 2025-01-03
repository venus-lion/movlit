package movlit.be.acceptance.movie_comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class MovieCommentSteps {

    public static ExtractableResponse<Response> 영화_코멘트_작성을_요청한다(String accessToken, String movieId,
                                                                RequestSpecification spec) {
        Map<String, Object> body = new HashMap<>();
        body.put("score", 2);
        body.put("comment", "이 영화는 정말 재미 없어");
        return 영화_코멘트를_작성한다(accessToken, movieId, spec, body);
    }

    public static ExtractableResponse<Response> 영화_코멘트를_작성한다(String accessToken, String movieId,
                                                             RequestSpecification spec, Map<String, Object> body) {
        return RestAssured
                .given()
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON_VALUE)
                .spec(spec)
                .log().all()
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .post("/api/movies/{movieId}/comments", movieId)
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_200이고_응답에_movieCommentId가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK));
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    public static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response, String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }

}
