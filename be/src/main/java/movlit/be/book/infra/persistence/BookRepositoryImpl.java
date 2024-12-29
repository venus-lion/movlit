package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.infra.persistence.jpa.BookJpaRepository;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.util.ids.BookId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final BookJpaRepository bookJpaRepository;

    @Override
    public Book findByBookId(BookId bookId) {
        BookEntity bookEntity = bookJpaRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        return BookConverter.toDomain(bookEntity);
    }

}
