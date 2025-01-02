package movlit.be.bookES;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/testBook")
public class BookESController {

    private final BookESBestsellerService bookESBestsellerService;
    @GetMapping("/elastic/saveBooks/bestseller")
    public void BestsellersApiToElasticSearch(){
        bookESBestsellerService.repeatGet(20); // 한번에 최대 50개씩, 20번 실행
    }
}
