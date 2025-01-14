package movlit.be.book;

import static movlit.be.book.BookMainSteps.베스트셀러_도서_리스트를_조회한다;
import static movlit.be.book.BookMainSteps.상태코드가_200_이고_응답데이터가_존재한다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("도서 메인 페이지 관련 인수테스트")
public class BookMainAcceptanceTest extends AcceptanceTest {

    @DisplayName("베스트셀러 영화 리스트를 가져오는데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_fetch_book_list_by_bestseller_then_response_200_and_bodt(){
        // docs
        api_문서_타이틀("fetchBookListByBestseller_success", spec);

        // given
        int limit = 5;

        // when
        var response = 베스트셀러_도서_리스트를_조회한다(limit, spec);

        // then
        상태코드가_200_이고_응답데이터가_존재한다(response);
    }
}
