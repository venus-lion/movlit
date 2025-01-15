package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookVo;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookGenreRepository {
    List<BookId> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);
}
