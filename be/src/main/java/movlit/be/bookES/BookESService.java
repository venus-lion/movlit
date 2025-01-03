package movlit.be.bookES;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookNewSpecial;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.infra.persistence.jpa.BookJpaRepository;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto;
import movlit.be.book.presentation.dto.BooksResponse.BookItemDto.WriterDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// Book 엔디티 -> BookES save 파싱하기
//# DB -> bookcrew의 role_type : ROLE_TYPE :: author만 필터링하기 (작가)값만 가져오게끔
@Service
@RequiredArgsConstructor
public class BookESService {
    private final BookESRepository bookESRepository;
    private final BookJpaRepository bookJpaRepository;


//    public void repeatGet(int times) {
//        for (int i = 1; i < times + 1; i++) {
//            saveBookESIndex();
//        }
//    }
    public void saveBookESIndex(){
        List<BookEntity> bookEntitiesAndRelated = bookJpaRepository.findBookEntitiesAndRelated();

        List<BookEntity> filteredBookEntities = bookEntitiesAndRelated.stream()
                .filter(bookEntity -> bookEntity.getBookId() != null && bookEntity.getBookId().getValue() != "")
                .collect(Collectors.toList());

        List<BookES> bookESList = filteredBookEntities.stream()
                .map(bookEntity -> convertToBookES(bookEntity))
                .collect(Collectors.toList());


        // bookES save - repository

        for (BookES bookES : bookESList){
            bookESRepository.save(bookES);
        }

    }

    // BookEntity -> BookES 바꿔주기
    public BookES convertToBookES(BookEntity bookEntity){

        String bookId = bookEntity.getBookId().getValue();


        List<String> crewList = bookEntity.getBookRCrewEntities().stream()
                .map(bookRCrewEntity -> bookRCrewEntity.getBookcrewEntity().getName())
                .collect(Collectors.toList());

        return BookES.builder()
                .bookId(bookId)
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .crew(crewList)
                .publisher(bookEntity.getPublisher())
                .pubDate(bookEntity.getPubDate())
                .description(bookEntity.getDescription())
                .categoryName(bookEntity.getCategoryName())
                .bookImgUrl(bookEntity.getBookImgUrl())
                .regDt(bookEntity.getRegDt())
                .updDt(bookEntity.getUpdDt())
                .build();
    }




}
