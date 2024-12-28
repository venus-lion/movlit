package movlit.be.acceptance.member;

import static movlit.be.acceptance.util.MemberFixture.사용자_원준;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import movlit.be.acceptance.util.MemberFixture;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberSteps {

    public static void 원준_회원가입() {
        회원가입한다(MemberFixture.사용자_원준_회원가입_요청(), new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    public static void 윤기_회원가입() {
        회원가입한다(MemberFixture.사용자_윤기_회원가입_요청(), new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    public static void 지원_회원가입() {
        회원가입한다(MemberFixture.사용자_지원_회원가입_요청(), new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    public static void 민지_회원가입() {
        회원가입한다(MemberFixture.사용자_민지_회원가입_요청(), new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }


    public static ExtractableResponse<Response> 비회원이_회원가입한다(RequestSpecification spec) {
        Map<String, Object> memberRegisterRequest = Map.of(
                "nickname", "원준정",
                "email", "wj@naver.com",
                "password", "qQQwe123!!",
                "repeatPassword", "qQQwe123!!",
                "dob", "1980-10-01");

        return 회원가입한다(memberRegisterRequest, spec);
    }

    public static ExtractableResponse<Response> 회원가입한다(Map<String, Object> memberRegisterRequest,
                                                       RequestSpecification spec) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .log().all()
                .body(memberRegisterRequest)
                .when()
                .post("/api/members/register")
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_201이고_응답에_memberId가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.CREATED),
                () -> assertThat(response.jsonPath().getObject("memberId", String.class)).isNotNull()
        );
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    public static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response, String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }

}
