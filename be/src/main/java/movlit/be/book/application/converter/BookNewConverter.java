package movlit.be.book.application.converter;

import movlit.be.book.domain.BookNewVo;
import movlit.be.book.domain.entity.BookNewEntity;

public class BookNewConverter {

    // Domain -> Entity
    public static BookNewEntity toEntity(BookNewVo bookNewVo) {
        return BookNewEntity.builder()
                .bookNewId(bookNewVo.getBookNewId())
                .bookEntity(BookConverter.toEntity(bookNewVo.getBookVo()))
                .build();
    }

    // Entity -> Domain
    public static BookNewVo toDomain(BookNewEntity bookNewEntity) {
        return BookNewVo.builder()
                .bookNewId(bookNewEntity.getBookNewId())
                .bookVo(BookConverter.toDomain(bookNewEntity.getBookEntity()))
                .build();
    }

}
