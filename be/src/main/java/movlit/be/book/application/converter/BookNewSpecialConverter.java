package movlit.be.book.application.converter;

import movlit.be.book.domain.BookNewSpecial;
import movlit.be.book.domain.entity.BookNewSpecialEntity;

public class BookNewSpecialConverter {
    // Domain -> Entity
    public static BookNewSpecialEntity toEntity(BookNewSpecial bookNewSpecial){
        return BookNewSpecialEntity.builder()
                .bookNewSpecialId(bookNewSpecial.getBookNewSpecialId())
                .bookEntity(BookConverter.toEntity(bookNewSpecial.getBook()))
                .build();
    }

    // Entity -> Domain
    public static BookNewSpecial toDomain(BookNewSpecialEntity bookNewSpecialEntity){
        return BookNewSpecial.builder()
                .bookNewSpecialId(bookNewSpecialEntity.getBookNewSpecialId())
                .book(BookConverter.toDomain(bookNewSpecialEntity.getBookEntity()))
                .build();
    }

}
