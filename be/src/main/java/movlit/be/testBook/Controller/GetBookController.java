package movlit.be.testBook.Controller;

import movlit.be.testBook.Entity.BookEntity;
import movlit.be.testBook.Repository.BookRepository;
import movlit.be.testBook.Service.GetBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetBookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GetBookService getBookService;

    @GetMapping("/saveBooks")
    public void String booksapiToDb(BookEntity book){
        getBookService.insertBook(book);

    }

}




