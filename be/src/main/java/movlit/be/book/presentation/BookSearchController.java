package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.service.BookSearchService;
import movlit.be.book.application.service.BooksRecommendationService;
import movlit.be.book.presentation.dto.BookRecommendDto;
import movlit.be.book.presentation.dto.BooksSearchResponseDto;
import movlit.be.common.util.ids.MemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/books/search")
public class BookSearchController {

    private final BookSearchService bookSearchService;
    private final BooksRecommendationService booksRecommendationService;

    /**
     * 로그인한 유저의 취향 장르 가져오기 -- elasticsearch 기반
     * */
    @GetMapping("/interestGenre")
    public ResponseEntity<List<BookRecommendDto>> fetchBookUserInterestByGenre(@AuthenticationPrincipal
                                                                               MyMemberDetails details) {
        MemberId memberId = details.getMemberId();
        List<BookRecommendDto> bookRecommendDtos = booksRecommendationService.fetchBookUserInterestByGenre(memberId);

        return ResponseEntity.ok(bookRecommendDtos);
    }

    /**
     * 로그인 유저의 최근 찜 도서 기반으로 유사한 도서 리스트 가져오기 -- elasticsearch 기반
     * */

    @GetMapping("/recommendations")
    public ResponseEntity<List<BookRecommendDto>> fetchRecommendedBooksByUserRecentHeart(
            @AuthenticationPrincipal MyMemberDetails details) {
        List<BookRecommendDto> bookRecommendDtos = booksRecommendationService.fetchRecommendedBooksByUserRecentHeart(
                details.getMemberId());
        return ResponseEntity.ok(bookRecommendDtos);
    }

    /**
     * 도서 검색 결과 - elasticsearch 기반
     * */
    @GetMapping("/searchBook")
    public ResponseEntity<BooksSearchResponseDto> fetchSearchBook(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam String inputStr) {
        BooksSearchResponseDto searchBook = bookSearchService.fetchSearchBook(inputStr, page, pageSize);

        return ResponseEntity.ok(searchBook);
    }

}
