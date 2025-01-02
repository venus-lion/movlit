package movlit.be.book.application.converter;

import java.util.List;
import java.util.stream.Collectors;
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

    // Domain list -> Entity list
    public static List<BookRCrewEntity> toEntityList(List<BookRCrew> bookRCrews){
        return bookRCrews.stream()
                .map(BookRCrewConverter::toEntity)
                .collect(Collectors.toList());
    }

    // Entity -> Domain
    public static BookRCrew toDomain(BookRCrewEntity bookRCrewEntity) {
        return BookRCrew.builder()
                .bookRCrewId(bookRCrewEntity.getBookRCrewId())
                //.book(BookConverter.toDomain(bookRCrewEntity.getBookEntity())) -- BookRCrew에서 BookEntity 필요 없음 -- 컨버터간 순환 참조 방지
                .bookcrew(BookcrewConverter.toDomain(bookRCrewEntity.getBookcrewEntity()))
                .build();
    }

    // Entity List -> Domain List
    public static List<BookRCrew> toDomainList(List<BookRCrewEntity> bookRCrewEntities){
        return bookRCrewEntities.stream()
                .map(BookRCrewConverter::toDomain)
                .collect(Collectors.toList());
    }

}
