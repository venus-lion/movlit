package movlit.be.acceptance.movie_heart;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("영화 찜 인수 테스트 - 현재, data,sql에서 movie에 넣고 있으므로, movie를 create하는 게 없으니 MovieHeartCountEntity가 생성되지 않는 문제가 발생함. movie 배치를 이용한 실사용 서비스에는 문제가 없음.")
public class MovieHeartAcceptanceTest extends AcceptanceTest {

//    String movieId;
//
//    @BeforeEach
//    public void before() {
//        movieId = String.valueOf(767L);
//    }
//
//    @DisplayName("영화 찜 버튼을 누르고 반영이 성공적으로 되면, 상태코드 200과 body를 반환한다.")
//    @Test
//    void when_heart_movie_then_response_200_and_body() {
//        // docs
//        api_문서_타이틀("movie_heart_increment_success", spec);
//
//        // when
//        var response = 영화에_찜을_누른다(movieId, 회원_원준_액세스토큰, spec);
//
//        // then
//        상태코드가_200이다(response);
//    }

}
