package movlit.be.book.application.converter;

import movlit.be.book.domain.BookHeartCountVo;
import movlit.be.book.domain.entity.BookHeartCountEntity;

public class BookHeartCountConverter {

    private BookHeartCountConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookHeartCountEntity toEntity(BookHeartCountVo bookHeartCountVo) {
        if (bookHeartCountVo == null) {
            return null;
        } else {
            return BookHeartCountEntity.builder()
                    .bookHeartCountId(bookHeartCountVo.getBookHeartCountId())
                    .bookEntity(BookDetailConverter.toEntity(bookHeartCountVo.getBookVo()))
                    .count(bookHeartCountVo.getCount())
                    .build();
        }
    }

    // Entity -> Domain
    public static BookHeartCountVo toDomain(BookHeartCountEntity bookHeartCountEntity) {
        if (bookHeartCountEntity == null) {
            return null;
        } else {
            return BookHeartCountVo.builder()
                    .bookHeartCountId(bookHeartCountEntity.getBookHeartCountId())
                    .bookVo(BookDetailConverter.toDomain(bookHeartCountEntity.getBookEntity()))
                    .count(bookHeartCountEntity.getCount())
                    .build();
        }

    }

}
