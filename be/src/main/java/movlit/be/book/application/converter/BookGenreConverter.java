package movlit.be.book.application.converter;

import java.util.List;
import java.util.stream.Collectors;
import movlit.be.book.domain.BookGenreVo;
import movlit.be.book.domain.entity.BookGenreEntity;
import movlit.be.book.domain.entity.BookGenreIdEntity;

public class BookGenreConverter {

    // Domain -> Entity
    public static BookGenreEntity toEntity(BookGenreVo bookGenreVo) {
        return BookGenreEntity.builder()
                .bookGenreIdEntity(new BookGenreIdEntity(bookGenreVo.getGenreId(), bookGenreVo.getBookVo().getBookId()))
                .bookEntity(BookConverter.toEntity(bookGenreVo.getBookVo()))
                .build();
    }

    // Domain List -> Entity List
    public static List<BookGenreEntity> toEntityList(List<BookGenreVo> bookGenreVos) {
        return bookGenreVos.stream()
                .map(BookGenreConverter::toEntity)
                .collect(Collectors.toList());
    }

    // Entity -> Domain
    public static BookGenreVo toDomain(BookGenreEntity bookGenreEntity) {
        return BookGenreVo.builder()
                .genreId(bookGenreEntity.getBookGenreIdEntity().getGenreId())
                // .book(BookConverter.toDomain(bookGenreEntity.getBookEntity())) -- 컨버터간 서로 호출해서 이거 없으면 stack-overflow 발생
                .build();
    }

    // Entity -> Domain List
    public static List<BookGenreVo> toDomainList(List<BookGenreEntity> bookGenreEntities) {
        return bookGenreEntities.stream()
                .map(BookGenreConverter::toDomain)
                .collect(Collectors.toList());
    }

}
