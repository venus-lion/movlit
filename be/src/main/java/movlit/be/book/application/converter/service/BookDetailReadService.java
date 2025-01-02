package movlit.be.book.application.converter.service;

import java.sql.SQLOutput;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookDetailResponseDto;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.domain.repository.BookcrewRepository;
import movlit.be.common.util.ids.BookId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDetailReadService {

    private final BookRepository bookRepository;
    private final BookcrewRepository bookcrewRepository;
    private final BookHeartRepository bookHeartRepository;

    public BookDetailResponseDto getBookDetail(BookId bookId) {
        Book book = findByBookId(bookId);
        long heartCount = countHeartsByBookId(bookId);
        List<Bookcrew> bookcrewList = findBookcrewByBook(book);
        BookDetailResponseDto bookDetailResponse = BookDetailResponseDto.builder()
                .bookId(book.getBookId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .publisher(book.getPublisher())
                .pubDate(book.getPubDate())
                .description(book.getDescription())
                .categoryName(book.getCategoryName())
                .bookImgUrl(book.getBookImgUrl())
                .stockStatus(book.getStockStatus())
                .mallUrl(book.getMallUrl())
                .heartCount(heartCount)
                .bookcrewList(bookcrewList)
                .build();

        return bookDetailResponse;
    }

    public Book findByBookId(BookId bookId) {
        return bookRepository.findByBookId(bookId);
    }

    public List<Bookcrew> findBookcrewByBook(Book book) {
        return bookcrewRepository.findByBook(book);
    }

    public long countHeartsByBookId(BookId bookId) {
        return bookHeartRepository.countHeartsByBookId(bookId);
    }


}
