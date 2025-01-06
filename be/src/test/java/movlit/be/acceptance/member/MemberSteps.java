package movlit.be.acceptance.member;

import static movlit.be.acceptance.util.MemberFixture.비회원;
import static movlit.be.acceptance.util.MemberFixture.회원_원준;
import static movlit.be.acceptance.util.MemberFixture.회원_지원;
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
                "dob", "1980-10-01",
                "genreIds", List.of(1L, 3L, 5L));

        return 회원가입한다(memberRegisterRequest, spec);
    }

    public static ExtractableResponse<Response> 회원_장르를_조회한다(RequestSpecification spec, String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .auth().oauth2(accessToken)
                .log().all()
                .when().get("/api/members/genreList")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_마이페이지를_조회한다(RequestSpecification spec, String accessToken) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .auth().oauth2(accessToken)
                .log().all()
                .when().get("/api/members/myPage")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원_장르를_조회한다(RequestSpecification spec) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .log().all()
                .when().get("/api/genreList")
                .then().log().all()
                .extract();
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

    public static ExtractableResponse<Response> 원준_회원을_수정을_요청한다(String accessToken, RequestSpecification spec) {
        Map<String, Object> memberUpdateRequest = Map.of(
                "nickname", 회원_지원.getNickname(),
                "email", 회원_지원.getEmail(),
                "password", 회원_지원.getPassword(),
                "repeatPassword", 회원_지원.getPassword(),
                "dob", 회원_지원.getDob(),
                "genreIds", 회원_지원.getGenreIds()
        );
        return 회원을_수정한다(accessToken, memberUpdateRequest, spec);
    }

    public static ExtractableResponse<Response> 회원을_수정한다(String accessToken, Map<String, Object> memberUpdateRequest,
                                                         RequestSpecification spec) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .spec(spec)
                .auth().oauth2(accessToken)
                .log().all()
                .body(memberUpdateRequest)
                .when()
                .put("/api/members/update")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비회원이_유효하지_않은_이메일을_입력하고_회원가입한다(
            RequestSpecification spec) {
        Map<String, Object> memberRegisterRequest = Map.of(
                "nickname", "원준정",
                "email", "mismatch",
                "password", "qQQwe123!!",
                "repeatPassword", "qQQwe123!!",
                "dob", "1980-10-01",
                "genreIds", List.of(1L, 3L, 5L));

        return 회원가입한다(memberRegisterRequest, spec);
    }

    public static ExtractableResponse<Response> 비회원이_회원_원준의_이메일로_회원가입한다(RequestSpecification spec) {
        Map<String, Object> memberRegisterRequest = Map.of(
                "nickname", 비회원.getNickname(),
                "email", 회원_원준.getEmail(),
                "password", 비회원.getPassword(),
                "repeatPassword", 비회원.getPassword(),
                "dob", 비회원.getDob(),
                "genreIds", List.of(1L, 3L, 5L));

        return 회원가입한다(memberRegisterRequest, spec);
    }

    public static ExtractableResponse<Response> 비회원이_회원_원준의_닉네임으로_회원가입한다(RequestSpecification spec) {
        Map<String, Object> memberRegisterRequest = Map.of(
                "nickname", 회원_원준.getNickname(),
                "email", 비회원.getEmail(),
                "password", 비회원.getPassword(),
                "repeatPassword", 비회원.getPassword(),
                "dob", 비회원.getDob(),
                "genreIds", List.of(1L, 3L, 5L));

        return 회원가입한다(memberRegisterRequest, spec);
    }

    public static ExtractableResponse<Response> 비회원이_일치하지_않는_재입력_패스워드로_회원가입한다(RequestSpecification spec) {
        Map<String, Object> memberRegisterRequest = Map.of(
                "nickname", 회원_원준.getNickname(),
                "email", 비회원.getEmail(),
                "password", 비회원.getPassword(),
                "repeatPassword", "QQqwe1234!",
                "dob", 비회원.getDob(),
                "genreIds", List.of(1L, 3L, 5L));

        return 회원가입한다(memberRegisterRequest, spec);
    }

    public static void 상태코드가_201이고_응답에_memberId가_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.CREATED),
                () -> assertThat(response.jsonPath().getObject("memberId", String.class)).isNotNull()
        );
    }

    public static void 상태코드가_200이고_응답에_genreId와_genreName이_존재한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK),
                () -> assertThat(response.jsonPath().getList("").get(0)).isNotNull(),
                () -> assertThat(response.jsonPath().getList("").get(1)).isNotNull()
        );
    }

    public static void 상태코드가_200이다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK)
        );
    }

    public static void 상태코드가_400이고_오류코드가_g001이고_errors에_email이_존재하는지_검증한다(
            ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.BAD_REQUEST),
                () -> 오류코드를_검증한다(response, "g001"),
                () -> assertThat(response.jsonPath().getMap("errors")).containsKeys("email")
        );
    }

    public static void 상태코드가_404이고_오류코드가_m002인지_검증한다(
            ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.NOT_FOUND),
                () -> 오류코드를_검증한다(response, "m002")
        );
    }

    public static void 상태코드가_404이고_오류코드가_m003인지_검증한다(
            ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.NOT_FOUND),
                () -> 오류코드를_검증한다(response, "m003")
        );
    }

    public static void 상태코드가_400이고_오류코드가_g001인지_검증한다(
            ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.BAD_REQUEST),
                () -> 오류코드를_검증한다(response, "g001")
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
