package movlit.be.book.application.converter;

import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.entity.BookEntity;

public class BookDetailConverter {

    private BookDetailConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookEntity toEntity(BookVo bookVo) {
        if (bookVo == null) {
            return null;
        } else {
            return BookEntity.builder()
                    .bookId(bookVo.getBookId())
                    .isbn(bookVo.getIsbn())
                    .title(bookVo.getTitle())
                    .publisher(bookVo.getPublisher())
                    .pubDate(bookVo.getPubDate())
                    .description(bookVo.getDescription())
                    .categoryName(bookVo.getCategoryName())
                    .bookImgUrl(bookVo.getBookImgUrl())
                    .stockStatus(bookVo.getStockStatus())
                    .mallUrl(bookVo.getMallUrl())
                    .heartCount(bookVo.getHeartCount())
                    .regDt(bookVo.getRegDt())
                    .updDt(bookVo.getUpdDt())
                    .build();
        }
    }

    // Entity -> Domain
    public static BookVo toDomain(BookEntity bookEntity) {
        if (bookEntity == null) {
            return null;
        } else {
            return movlit.be.book.domain.BookVo.builder()
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
                    .build();
        }
    }

}

