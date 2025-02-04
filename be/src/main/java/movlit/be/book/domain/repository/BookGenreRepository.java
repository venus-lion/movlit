package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;

public interface BookGenreRepository {

    List<BookId> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);

}
