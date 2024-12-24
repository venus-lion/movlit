package movlit.be.testBook.Controller;

import lombok.RequiredArgsConstructor;
import movlit.be.testBook.Dto.BookResponseDto;
import movlit.be.testBook.Service.ApiTestService;
import movlit.be.testBook.Service.GetBookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetBookController {

    private final GetBookService getBookService;
    private final ApiTestService apiTestService;

    @GetMapping("/saveBooks")
    public void booksapiToDb(){
        getBookService.insertBook();
    }

    @GetMapping("/testBookapi")
    public void testbookapi(){
        apiTestService.testapiBook();
    }
}




