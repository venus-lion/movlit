package movlit.be.book.infra.persistence;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.repository.BookGenreRepository;
import movlit.be.book.infra.persistence.jpa.BookGenreJpaRepository;
import movlit.be.common.exception.BooksByGenreNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookGenreRepositoryImpl implements BookGenreRepository {
    private final BookGenreJpaRepository bookGenreJpaRepository;

    @Override
    public List<Book> findBooksByGenreIds(List<Long> genreIds, Pageable pageable) {
        List<BookEntity> booksByGenreIds = bookGenreJpaRepository.findBooksByGenreIds(genreIds, pageable);

        if (booksByGenreIds.isEmpty()){
            throw new BooksByGenreNotFoundException();
        }

        return booksByGenreIds.stream()
                .map(BookConverter::toDomain)
                .collect(Collectors.toList());
    }

}
