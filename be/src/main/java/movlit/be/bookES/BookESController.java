package movlit.be.bookES;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/testBook")
public class BookESController {


    private final BookESService bookESService; // DB -> ES

    @GetMapping("/elastic/DB/ES")
    public void saveBookDBtoES(){
        bookESService.saveBookESIndex();
    }
}
