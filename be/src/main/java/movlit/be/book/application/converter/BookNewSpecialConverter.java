package movlit.be.book.application.converter;

import movlit.be.book.domain.BookNewSpecialVo;
import movlit.be.book.domain.entity.BookNewSpecialEntity;

public class BookNewSpecialConverter {
    // Domain -> Entity
    public static BookNewSpecialEntity toEntity(BookNewSpecialVo bookNewSpecialVo){
        return BookNewSpecialEntity.builder()
                .bookNewSpecialId(bookNewSpecialVo.getBookNewSpecialId())
                .bookEntity(BookConverter.toEntity(bookNewSpecialVo.getBookVo()))
                .build();
    }

    // Entity -> Domain
    public static BookNewSpecialVo toDomain(BookNewSpecialEntity bookNewSpecialEntity){
        return BookNewSpecialVo.builder()
                .bookNewSpecialId(bookNewSpecialEntity.getBookNewSpecialId())
                .bookVo(BookConverter.toDomain(bookNewSpecialEntity.getBookEntity()))
                .build();
    }

}
