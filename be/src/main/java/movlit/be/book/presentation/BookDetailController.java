package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.book.application.converter.service.BookDetailReadService;
import movlit.be.book.domain.Book;
import movlit.be.common.util.ids.BookId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookDetailController {
    private final BookDetailReadService bookDetailReadService;


    @GetMapping("{bookId}/detail")
    @ResponseBody
    public Book findBook(@PathVariable BookId bookId){
        Book book = bookDetailReadService.findByBookId(bookId);
        return book;
    }

}
