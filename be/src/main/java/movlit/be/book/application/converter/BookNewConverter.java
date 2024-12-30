package movlit.be.book.application.converter;

import movlit.be.book.domain.BookNew;
import movlit.be.book.domain.entity.BookNewEntity;

public class BookNewConverter {
    // Domain -> Entity
    public static BookNewEntity toEntity(BookNew bookNew){
        return BookNewEntity.builder()
                .bookNewId(bookNew.getBookNewId())
                .bookEntity(BookConverter.toEntity(bookNew.getBook()))
                .build();
    }

    // Entity -> Domain
    public static BookNew toDomain(BookNewEntity bookNewEntity){
        return BookNew.builder()
                .bookNewId(bookNewEntity.getBookNewId())
                .book(BookConverter.toDomain(bookNewEntity.getBookEntity()))
                .build();
    }

}
