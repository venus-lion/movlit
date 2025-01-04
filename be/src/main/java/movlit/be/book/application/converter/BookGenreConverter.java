package movlit.be.book.application.converter;

import movlit.be.book.domain.BookGenre;
import movlit.be.book.domain.entity.BookGenreEntity;
import movlit.be.book.domain.entity.BookGenreIdEntity;

public class BookGenreConverter {
    // Domain -> Entity
    public static BookGenreEntity toEntity(BookGenre bookGenre){
        return BookGenreEntity.builder()
                .bookGenreIdEntity(new BookGenreIdEntity(bookGenre.getGenreId(), bookGenre.getBook().getBookId()))
                .bookEntity(BookConverter.toEntity(bookGenre.getBook()))
                .build();
    }

    // Entity -> Domain
    public static BookGenre toDomain(BookGenreEntity bookGenreEntity){
        return BookGenre.builder()
                .genreId(bookGenreEntity.getBookGenreIdEntity().getGenreId())
                .book(BookConverter.toDomain(bookGenreEntity.getBookEntity()))
                .build();
    }
}
