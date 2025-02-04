package movlit.be.acceptance.image;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.IOException;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ImageSteps {

    public static ExtractableResponse<Response> 프로필_이미지를_업로드한다(String accessToken, RequestSpecification spec) {
        return RestAssured.given().spec(spec)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE).multiPart(getFile("images/potato.jpg"))
                .auth().oauth2(accessToken)
                .log().all()
                .when()
                .post("/api/images/profile")
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_200임을_검증한다(ExtractableResponse<Response> response) {
        상태코드를_검증한다(response, HttpStatus.OK);
    }

    public static void 상태코드가_400임을_검증한다(ExtractableResponse<Response> response) {
        상태코드를_검증한다(response, HttpStatus.BAD_REQUEST);
    }

    public static void 상태코드가_200이고_응답에_id와_url이_존재함을_검증한다(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK),
                () -> assertThat(response.jsonPath().getString("imageId")).isNotBlank(),
                () -> assertThat(response.jsonPath().getString("url")).isNotBlank()
        );
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus) {
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }

    static File getFile(String path) {
        Resource resource = new ClassPathResource(path);
        try {
            return resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static AbstractStringAssert<?> 오류코드를_검증한다(ExtractableResponse<Response> response, String code) {
        return assertThat(response.jsonPath().getString("code")).isEqualTo(code);
    }

}
