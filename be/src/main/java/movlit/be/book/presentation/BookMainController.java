package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.service.BookMainReadService;
import movlit.be.book.presentation.dto.BooksResponse;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto;
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
    public BooksResponse getTopBestsellers(@RequestParam(defaultValue = "30") int limit){
        List<BookItemDto> bookBestsellerDtos = bookMainReadService.getTopBestsellers(limit);

        return BooksResponse.builder()
                .books(bookBestsellerDtos)
                .build();
    }

    @GetMapping("/new")
    public BooksResponse getRecentBookNew(@RequestParam(defaultValue = "30") int limit){
        List<BookItemDto> bookNewDtos = bookMainReadService.getRecentBookNew(limit);

        return BooksResponse.builder()
                .books(bookNewDtos)
                .build();
    }

    @GetMapping("/popular")
    public BooksResponse getPopularBookNewSpecial(@RequestParam(defaultValue = "30") int limit){
        List<BookItemDto> bookNewSpecialsDtos = bookMainReadService.getPopularBookNewSpecial(limit);

        return BooksResponse.builder()
                .books(bookNewSpecialsDtos)
                .build();
    }
}
