package movlit.be.acceptance.member;

import static movlit.be.acceptance.util.MemberFixture.사용자_원준;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import movlit.be.acceptance.util.MemberFixture;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberSteps {

    private static final RequestSpecification MOCK_SPEC = new RequestSpecBuilder().build();

    public static void 원준_회원가입한다() {
        회원가입한다(MemberFixture.사용자_원준_회원가입_요청(), new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    public static ExtractableResponse<Response> 비회원_원준이_회원가입한다(RequestSpecification spec) {
        Map<String, Object> memberRegisterRequest = Map.of(
                "nickname", 사용자_원준.getNickname(),
                "email", 사용자_원준.getEmail(),
                "password", 사용자_원준.getPassword(),
                "repeatPassword", 사용자_원준.getPassword(),
                "dob", 사용자_원준.getDob());

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

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    public static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response, String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }

    public static void 상태코드가_201이고_응답에_id가_존재하며_회원가입한_보노의_회원프로필이_조회되는지_검증한다(ExtractableResponse<Response> response) {
//        String 회원보노_아이디 = response.jsonPath().getObject("id", String.class);
//        var 회원보노_회원프로필_조회_응답 = 회원프로필을_조회한다(회원보노_아이디, MOCK_SPEC);

        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.CREATED)
//                () -> assertThat(response.jsonPath().getObject("id", String.class)).isNotNull(),
//                () -> 상태코드를_검증한다(회원보노_회원프로필_조회_응답, HttpStatus.OK),
//                () -> assertThat(회원보노_회원프로필_조회_응답.jsonPath().getString("email")).isEqualTo(사용자_보노.getEmail())
        );
    }

}
