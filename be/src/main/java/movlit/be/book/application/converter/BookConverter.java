package movlit.be.book.application.converter;

import java.time.LocalDateTime;
import movlit.be.common.util.ids.BookId;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.entity.BookEntity;

public class BookConverter {

    private BookConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }



    // Domain -> Entity
    public static BookEntity toEntity(Book book) {
        return BookEntity.builder()
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
                .heartCount(book.getHeartCount())
                .regDt(book.getRegDt())
                .updDt(book.getUpdDt())
                .bookRCrewEntities(BookRCrewConverter.toEntityList(book.getBookRCrews()))
                .build();
    }

    // Entity -> Domain
    public static Book toDomain(BookEntity bookEntity) {
        return Book.builder()
                .bookId(bookEntity.getBookId())
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .publisher(bookEntity.getPublisher())
                .pubDate(bookEntity.getPubDate())
                .description(bookEntity.getDescription())
                .categoryName(bookEntity.getCategoryName())
                .bookImgUrl(bookEntity.getBookImgUrl())
                .stockStatus(bookEntity.getStockStatus())
                .mallUrl(bookEntity.getMallUrl())
                .heartCount(bookEntity.getHeartCount())
                .regDt(bookEntity.getRegDt())
                .updDt(bookEntity.getUpdDt())
                .bookRCrews(BookRCrewConverter.toDomainList(bookEntity.getBookRCrewEntities()))
                .build();
    }

}
