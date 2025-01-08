package movlit.be.book.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.presentation.dto.BookDetailResponseDto;
import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.repository.BookHeartCountRepository;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.domain.repository.BookcrewRepository;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDetailReadService {

    private final BookRepository bookRepository;
    private final BookcrewRepository bookcrewRepository;
    private final BookHeartRepository bookHeartRepository;
    private final BookHeartCountRepository bookHeartCountRepository;

    // 도서 상세 정보
    public BookDetailResponseDto getBookDetail(BookId bookId, Member member) {
        Book book = findByBookId(bookId);
        int heartCount = countHeartsByBookId(bookId);
        boolean isHearted = false;
        if(member != null)
            isHearted = isHeartedByBook(book, member);
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
                .isHearted(isHearted)
                .bookcrewList(bookcrewList)
                .build();

        return bookDetailResponse;
    }

    public Book findByBookId(BookId bookId) {
        return bookRepository.findByBookId(bookId);
    }

    // 해당 책의 크루
    public List<Bookcrew> findBookcrewByBook(Book book) {
        return bookcrewRepository.findByBook(book);
    }

    // 찜 갯수
    public int countHeartsByBookId(BookId bookId) {
        return bookHeartCountRepository.countHeartByBookId(bookId);
    }

    // 해당 책 나의 찜 여부
    public boolean isHeartedByBook(Book book, Member member){
         if(bookHeartRepository.findByBookAndMember(book, member) != null)
             return true;
         else
             return false;
    }


}
