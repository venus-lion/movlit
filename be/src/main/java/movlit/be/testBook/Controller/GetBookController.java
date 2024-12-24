package movlit.be.testBook.Controller;

import lombok.RequiredArgsConstructor;
import movlit.be.testBook.Service.GetBookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetBookController {

    private final GetBookService getBookService;

    @GetMapping("/saveBooks")
    public void booksapiToDb(){
        getBookService.repeatGet(20); // 한번에 최대 50개씩, 20번 실행
    }

}




