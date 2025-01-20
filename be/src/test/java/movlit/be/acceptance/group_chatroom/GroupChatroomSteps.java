package movlit.be.acceptance.group_chatroom;

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

public class GroupChatroomSteps {

    public static ExtractableResponse<Response> 그룹_채팅_생성을_요청한다(String accessToken,
                                                               RequestSpecification spec) {
        /**
         *     @NotBlank(message = "채팅방 제목은 항상 존재해야 합니다.")
         *     private String roomName;
         *
         *     @NotBlank(message = "(개발자 알림) 컨텐츠 유형이 비어 있습니다.")
         *     private String contentType; // movie, book
         *
         *     @NotNull(message = "컨텐츠의 id가 없습니다.")
         *     private Long contentId;
         */

        Map<String, Object> body = new HashMap<>();
        body.put("roomName", "해리포터 활동방");
        body.put("contentType", "movie");
        body.put("contentId", 767L);

        return 그룹_채팅을_생성한다(accessToken, spec, body);
    }

    public static ExtractableResponse<Response> 그룹_채팅을_생성한다(String accessToken,
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
                .post("/api/chat/create/group")
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
