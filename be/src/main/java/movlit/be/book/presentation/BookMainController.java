package movlit.be.book.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.service.BookMainReadService;
import movlit.be.book.application.service.BooksRecommendationService;
import movlit.be.book.application.service.GetBooksByRandomGenreService;
import movlit.be.book.presentation.dto.BooksGenreResponse;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto;
import movlit.be.book.presentation.dto.BooksResponse;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESDomain;
import movlit.be.common.util.ids.MemberId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final BooksRecommendationService booksRecommendationService;

    @GetMapping("/bestseller")
    public ResponseEntity<BooksResponse> getTopBestsellers(@RequestParam(defaultValue = "30") int limit){
        List<BookItemDto> bookBestsellerDtos = bookMainReadService.getTopBestsellers(limit);

        BooksResponse booksResponse = BooksResponse.builder()
                .books(bookBestsellerDtos)
                .build();

        return ResponseEntity.ok(booksResponse);
    }

    @GetMapping("/new")
    public ResponseEntity<BooksResponse> getRecentBookNew(@RequestParam(defaultValue = "30") int limit){
        List<BookItemDto> bookNewDtos = bookMainReadService.getRecentBookNew(limit);

        BooksResponse booksResponse = BooksResponse.builder()
                .books(bookNewDtos)
                .build();

        return ResponseEntity.ok(booksResponse);
    }

    @GetMapping("/popular")
    public ResponseEntity<BooksResponse> getPopularBookNewSpecial(@RequestParam(defaultValue = "30") int limit){
        List<BookItemDto> bookNewSpecialsDtos = bookMainReadService.getPopularBookNewSpecial(limit);

        BooksResponse booksResponse = BooksResponse.builder()
                .books(bookNewSpecialsDtos)
                .build();

        return ResponseEntity.ok(booksResponse);
    }

    // 랜덤장르 불러오기 -> dto가 달라짐
    @GetMapping("/genres/random")
    public ResponseEntity<BooksGenreResponse> getBooksByRandomGenre(@RequestParam(defaultValue = "30") int limit){
        List<BookItemWithGenreDto> booksByRandomGenresDto = getBooksByRandomGenreService.getBooksByRandomGenres(limit);

        BooksGenreResponse booksGenreResponse = BooksGenreResponse.builder()
                .bookWithGenres(booksByRandomGenresDto)
                .build();

        return ResponseEntity.ok(booksGenreResponse);
    }


    // 로그인 했을 때, 멤버의 MemberGenre 3개 중 랜덤 2개 선택 + 나머지 랜덤장르 1개, 총 3개의 BooksGenreResponse 불러오기
    @GetMapping("/genres/personalized")
    public ResponseEntity<BooksGenreResponse> getBooksByPersonalizedRandomGenre(
            @AuthenticationPrincipal MyMemberDetails details,
            @RequestParam(defaultValue = "30") int limit
            ){

        System.out.println("details :: memberId >> " + details.getMemberId().getValue());
        System.out.println("details로부터 가져온 Member id :: " + details.getMemberId());

        MemberId memberId = details.getMemberId();
        List<BookItemWithGenreDto> booksByPersonalizedRandomGenre = getBooksByRandomGenreService.getBooksByPersonalizedRandomGenre(
                limit, memberId);
        BooksGenreResponse booksGenreResponse = BooksGenreResponse.builder()
                .bookWithGenres(booksByPersonalizedRandomGenre)
                .build();

        return ResponseEntity.ok(booksGenreResponse);
    }


    // 로그인 유저가 최근에 찜한 도서 기반으로, elastic을 이용한 사용자 맞춤형 도서 추천
    // dto 추가해야 함
    @GetMapping("/recommendations")
    public ResponseEntity<List<BookESDomain>> getRecommendations(@AuthenticationPrincipal MyMemberDetails details){
        System.out.println("/recommendationis, details ::: " + details);
        List<BookESDomain> recommendedBook = booksRecommendationService.getRecommendedBook(details.getMemberId());
        return ResponseEntity.ok(recommendedBook);
    }

}
