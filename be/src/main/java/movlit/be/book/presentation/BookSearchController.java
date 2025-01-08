package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.service.BookSearchService;
import movlit.be.book.presentation.dto.BooksSearchResponseDto;
import movlit.be.bookES.BookESDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookSearchController {
    private final BookSearchService bookSearchService;
    /*
       도서 검색 결과 -- /searchBook
     */
    @GetMapping("/searchBook")
    public ResponseEntity<BooksSearchResponseDto> getSearchBook(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam String inputStr){
        BooksSearchResponseDto searchBook = bookSearchService.getSearchBook(inputStr, page, pageSize);

        return ResponseEntity.ok(searchBook);
    }

}
