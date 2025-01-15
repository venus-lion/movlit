package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.service.BookMainReadService;
import movlit.be.book.application.service.GetBooksByRandomGenreService;
import movlit.be.book.presentation.dto.BooksGenreResponse;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto;
import movlit.be.book.presentation.dto.BooksResponse;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookMainController {

    private final BookMainReadService bookMainReadService;
    private final GetBooksByRandomGenreService getBooksByRandomGenreService;

    @GetMapping("/bestseller")
    public ResponseEntity<BooksResponse> fetchBestsellers(@RequestParam(defaultValue = "30") int limit) {
        List<BookItemDto> bookBestsellerDtos = bookMainReadService.fetchBestsellers(limit);

        BooksResponse booksResponse = BooksResponse.builder()
                .books(bookBestsellerDtos)
                .build();

        return ResponseEntity.ok(booksResponse);
    }

    @GetMapping("/new")
    public ResponseEntity<BooksResponse> fetchBookNews(@RequestParam(defaultValue = "30") int limit) {
        List<BookItemDto> bookNewDtos = bookMainReadService.fetchBookNews(limit);

        BooksResponse booksResponse = BooksResponse.builder()
                .books(bookNewDtos)
                .build();

        return ResponseEntity.ok(booksResponse);
    }

    @GetMapping("/popular")
    public ResponseEntity<BooksResponse> fetchBookNewSpecials(@RequestParam(defaultValue = "30") int limit) {
        List<BookItemDto> bookNewSpecialsDtos = bookMainReadService.fetchBookNewSpecials(limit);

        BooksResponse booksResponse = BooksResponse.builder()
                .books(bookNewSpecialsDtos)
                .build();

        return ResponseEntity.ok(booksResponse);
    }

    // 랜덤장르 불러오기
    // 얘는 '마니아를 위해' : [장르명], [장르명], [장르명]
    @GetMapping("/genres/random")
    public ResponseEntity<BooksGenreResponse> fetchBooksByRandomGenre(@RequestParam(defaultValue = "30") int limit) {
        List<BookItemWithGenreDto> booksByRandomGenresDto = getBooksByRandomGenreService.fetchBooksByRandomGenre(limit);

        BooksGenreResponse booksGenreResponse = BooksGenreResponse.builder()
                .books(booksByRandomGenresDto)
                .build();

        return ResponseEntity.ok(booksGenreResponse);
    }


}
