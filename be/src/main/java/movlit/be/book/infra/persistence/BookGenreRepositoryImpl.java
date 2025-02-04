package movlit.be.book.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.repository.BookGenreRepository;
import movlit.be.book.infra.persistence.jpa.BookGenreJpaRepository;
import movlit.be.common.exception.BooksByGenreNotFoundException;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookGenreRepositoryImpl implements BookGenreRepository {

    private final BookGenreJpaRepository bookGenreJpaRepository;

    @Override
    public List<BookId> findBooksByGenreIds(List<Long> genreIds, Pageable pageable) {
        List<BookId> bookIdsByGenreIds = bookGenreJpaRepository.findBooksByGenreIds(genreIds, pageable);

        if (bookIdsByGenreIds.isEmpty()) {
            throw new BooksByGenreNotFoundException();
        }

        return bookIdsByGenreIds;
    }

}
