package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.bookES.BookESDomain;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/books/")
public class BookSearchController {
    /*
       도서 검색 결과 -- /searchBook
     */
    @GetMapping("/searchBook")
    public ResponseEntity<List<BookESDomain>> getSearchBook(@RequestParam String inputStr){
        return null;
    }

}
