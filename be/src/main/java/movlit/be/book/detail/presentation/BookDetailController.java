package movlit.be.book.detail.presentation;

import movlit.be.book.detail.application.service.BookDetailReadService;
import movlit.be.book.detail.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api/books/")
public class BookDetailController {

    private BookDetailReadService bookDetailReadService;

    @GetMapping("{bookId}/detail")
    @ResponseBody
    public Book getBookDetail(@PathVariable String bookId){
        return bookDetailReadService.findByBookId(bookId);
    }

}
