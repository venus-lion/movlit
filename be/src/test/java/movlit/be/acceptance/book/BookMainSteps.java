package movlit.be.acceptance.book;

import co.elastic.clients.elasticsearch.nodes.Http;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.AbstractIntegerAssert;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
public class BookMainSteps {

    /*
     * 인기순 도서리스트를 조회한다.
     */
    public static ExtractableResponse<Response> 베스트셀러_도서_리스트를_조회한다(
            int limit,
            RequestSpecification spec
    ){
        return RestAssured
                .given()
                .param("limit", limit)
                .spec(spec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .get("/api/books/bestseller")
                .then()
                .log().all()
                .extract();
    }

    public static void 상태코드가_200_이고_응답데이터가_존재한다(ExtractableResponse<Response> response){
        Assertions.assertAll(
                () -> 상태코드를_검증한다(response, HttpStatus.OK),
                () -> assertThat(response).isNotNull()
        );
    }

    public static AbstractIntegerAssert<?> 상태코드를_검증한다(ExtractableResponse<Response> response,
                                                      HttpStatus expectedHttpStatus){
        return assertThat(response.statusCode()).isEqualTo(expectedHttpStatus.value());
    }
}

