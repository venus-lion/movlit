package movlit.be.book.application.converter;

import java.util.List;
import java.util.stream.Collectors;
import movlit.be.book.domain.BookRCrewVo;
import movlit.be.book.domain.entity.BookRCrewEntity;

public class BookRCrewConverter {

    private BookRCrewConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookRCrewEntity toEntity(BookRCrewVo bookRCrewVo) {
        return BookRCrewEntity.builder()
                .bookRCrewId(bookRCrewVo.getBookRCrewId())
                //.book(BookConverter.toEntity(bookRCrew.getBook()))
                .bookcrewEntity(BookcrewConverter.toEntity(bookRCrewVo.getBookcrewVo()))
                .build();
    }

    // Domain list -> Entity list
    public static List<BookRCrewEntity> toEntityList(List<BookRCrewVo> bookRCrewVos){
        return bookRCrewVos.stream()
                .map(BookRCrewConverter::toEntity)
                .collect(Collectors.toList());
    }

    // Entity -> Domain
    public static BookRCrewVo toDomain(BookRCrewEntity bookRCrewEntity) {
        return BookRCrewVo.builder()
                .bookRCrewId(bookRCrewEntity.getBookRCrewId())
                //.book(BookConverter.toDomain(bookRCrewEntity.getBook())) -- BookRCrew에서 BookEntity 필요 없음 -- 컨버터간 순환 참조 방지
                .bookcrewVo(BookcrewConverter.toDomain(bookRCrewEntity.getBookcrewEntity()))
                .build();
    }

    // Entity List -> Domain List
    public static List<BookRCrewVo> toDomainList(List<BookRCrewEntity> bookRCrewEntities){
        return bookRCrewEntities.stream()
                .map(BookRCrewConverter::toDomain)
                .collect(Collectors.toList());
    }

}
