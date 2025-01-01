package movlit.be.book.application.converter;

import movlit.be.book.domain.BookRCrew;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.common.util.ids.BookRCrewId;

public class BookRCrewConverter {

    private BookRCrewConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookRCrewEntity toEntity(BookRCrew bookRCrew) {
        return BookRCrewEntity.builder()
                .bookRCrewId(bookRCrew.getBookRCrewId())
                .bookEntity(BookConverter.toEntity(bookRCrew.getBook()))
                .bookcrewEntity(BookcrewConverter.toEntity(bookRCrew.getBookcrew()))
                .build();
    }

    // Entity -> Domain
    public static BookRCrew toDomain(BookRCrewEntity bookRCrewEntity) {
        return BookRCrew.builder()
                .bookRCrewId(bookRCrewEntity.getBookRCrewId())
                .book(BookConverter.toDomain(bookRCrewEntity.getBookEntity()))
                .bookcrew(BookcrewConverter.toDomain(bookRCrewEntity.getBookcrewEntity()))
                .build();
    }

}
