package movlit.be.acceptance;

import static movlit.be.acceptance.auth.AuthSteps.민지가_로그인한다;
import static movlit.be.acceptance.auth.AuthSteps.원준이_로그인한다;
import static movlit.be.acceptance.auth.AuthSteps.윤기가_로그인한다;
import static movlit.be.acceptance.auth.AuthSteps.지원이_로그인한다;
import static movlit.be.acceptance.member.MemberSteps.민지_회원가입;
import static movlit.be.acceptance.member.MemberSteps.원준_회원가입;
import static movlit.be.acceptance.member.MemberSteps.윤기_회원가입;
import static movlit.be.acceptance.member.MemberSteps.지원_회원가입;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import jakarta.annotation.PostConstruct;
import java.util.List;
import movlit.be.acceptance.util.DatabaseCleanup;
import movlit.be.acceptance.util.SqlFileExecutor;
import movlit.be.acceptance.util.TableCleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
public abstract class AcceptanceTest {

    public static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0");
    public static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7.2");
    public static final MySQLContainer<?> MYSQL = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("test").withUsername("root").withPassword("1234").withReuse(true);
    public static final GenericContainer<?> REDIS = new GenericContainer<>(REDIS_IMAGE).withReuse(true);

    static {
        MYSQL.setPortBindings(List.of("3307:3306"));
        REDIS.setPortBindings(List.of("6379:6379"));
    }

    @LocalServerPort
    public int port;

    public String 회원_원준_액세스토큰;
    public String 회원_지원_액세스토큰;
    public String 회원_민지_액세스토큰;
    public String 회원_윤기_액세스토큰;

    @Autowired
    protected DatabaseCleanup databaseCleanup;

    @Autowired
    protected TableCleanup tableCleanup;

    @Autowired
    protected SqlFileExecutor sqlFileExecutor;
    protected RequestSpecification spec;

    public static void api_문서_타이틀(String documentName, RequestSpecification specification) {
        specification.filter(document(
                documentName,
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        ));
    }

    @PostConstruct
    public void setRestAssuredPort() {
        RestAssured.port = port;
    }

    protected void 데이터베이스를_초기화한다() {
        databaseCleanup.execute();
        sqlFileExecutor.execute("data_mainTest.sql");
    }

    protected void 테이블을_비운다(String tableName) {
        tableCleanup.setTableName(tableName);
        tableCleanup.execute();
    }

    @BeforeEach
    void setSpec(RestDocumentationContextProvider provider) {
        데이터베이스를_초기화한다();
        initAccessToken();
        this.spec = new RequestSpecBuilder().addFilter(
                RestAssuredRestDocumentation.documentationConfiguration(provider)).build();
    }

    @BeforeAll
    static void startContainer() {
        MYSQL.start();
        REDIS.start();
    }

    private void initAccessToken() {
        회원_원준_액세스토큰 = 원준_액세스토큰_요청();
        회원_민지_액세스토큰 = 민지_액세스토큰_요청();
        회원_윤기_액세스토큰 = 윤기_액세스토큰_요청();
        회원_지원_액세스토큰 = 지원_액세스토큰_요청();
    }

    private static String 원준_액세스토큰_요청() {
        원준_회원가입();
        return 원준이_로그인한다(new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    private static String 민지_액세스토큰_요청() {
        민지_회원가입();
        return 민지가_로그인한다(new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    private static String 윤기_액세스토큰_요청() {
        윤기_회원가입();
        return 윤기가_로그인한다(new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

    private static String 지원_액세스토큰_요청() {
        지원_회원가입();
        return 지원이_로그인한다(new RequestSpecBuilder().build()).jsonPath().getString("accessToken");
    }

}
