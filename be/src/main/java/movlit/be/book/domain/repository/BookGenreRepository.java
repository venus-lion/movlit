package movlit.be.book.domain.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import movlit.be.book.domain.Book;

public interface BookGenreRepository {
    List<Book> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);
}
