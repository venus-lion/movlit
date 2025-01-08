package movlit.be.book.application.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookBestseller;
import movlit.be.book.domain.BookNew;
import movlit.be.book.domain.BookNewSpecial;
import movlit.be.book.domain.repository.BookBestsellerRepository;
import movlit.be.book.domain.repository.BookNewRepository;
import movlit.be.book.domain.repository.BookNewSpecialRepository;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto.WriterDto;
import movlit.be.common.exception.BookIllegalArgumentException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMainReadService {
    private final BookBestsellerRepository bookBestsellerRepository;
    private final BookNewRepository bookNewRepository;
    private final BookNewSpecialRepository bookNewSpecialRepository;

    public List<BookItemDto> getTopBestsellers(int limit){
        Pageable pageable = PageRequest.of(0, limit); // 0페이지부터 limit 개만큼 가져옴
        List<BookBestseller> bookbestsellers = bookBestsellerRepository.findAllBestsellers(pageable);

        return bookbestsellers.stream()
                .map(bookbestseller -> convertToDto(bookbestseller))
                .collect(Collectors.toList());
    }

    public List<BookItemDto> getRecentBookNew(int limit){
        Pageable pageable = PageRequest.of(0, limit); // 0페이지 ~ limit 개수만큼 가져옴
        List<BookNew> bookNews = bookNewRepository.findAllBookNew(pageable);

        return bookNews.stream()
                .map(bookNew -> convertToDto(bookNew))
                .collect(Collectors.toList());
    }

    public List<BookItemDto> getPopularBookNewSpecial(int limit){
        Pageable pageable = PageRequest.of(0, limit);
        List<BookNewSpecial> bookNewSpecials = bookNewSpecialRepository.findAllBookNewSpecial(pageable);

        return bookNewSpecials.stream()
                .map(bookNewSpecial -> convertToDto(bookNewSpecial))
                .collect(Collectors.toList());
    }

    // 공통된 Book -> BookItemDto 변환 로직
    private BookItemDto convertToDto(Object bookObject){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // BookBestseller, BookNew, BookNewSpecial 일 때에 따라 다른 타입으로 변환
        Book book = getBookFromEntity(bookObject);

        List<WriterDto> writers = book.getBookRCrews().stream()
                .map(rc -> WriterDto.builder()
                        .name(rc.getBookcrew().getName())
                        .role(rc.getBookcrew().getRole().name())
                        .build())
                .collect(Collectors.toList());

        return BookItemDto.builder()
                .bookId(book.getBookId().getValue())
                .title(book.getTitle())
                .writers(writers)
                .pubDate(book.getPubDate().format(formatter))
                .bookImgUrl(book.getBookImgUrl())
                .build();

    }

    // Entity에 맞는 Book 객체를 가져옴
    private Book getBookFromEntity(Object bookEntity){
        if (bookEntity instanceof BookBestseller){
            return ((BookBestseller)bookEntity).getBook();
        } else if (bookEntity instanceof BookNew) {
            return ((BookNew)bookEntity).getBook();
        } else if (bookEntity instanceof BookNewSpecial){
            return ((BookNewSpecial)bookEntity).getBook();
        }
        else {
            throw new BookIllegalArgumentException();
        }
    }
}
