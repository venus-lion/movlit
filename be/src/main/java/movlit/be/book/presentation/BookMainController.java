package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.service.BookMainReadService;
import movlit.be.book.presentation.dto.BookBestsellersResponse;
import movlit.be.book.presentation.dto.BookBestsellersResponse.BookBestsellerDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookMainController {
    public final BookMainReadService bookMainReadService;

    @GetMapping("/bestseller")
    public BookBestsellersResponse getTopBestsellers(@RequestParam(defaultValue = "30") int limit){
        List<BookBestsellerDto> bookBestsellerDtos = bookMainReadService.getTopBestsellers(limit);

        return BookBestsellersResponse.builder()
                .books(bookBestsellerDtos)
                .build();
    }
}
