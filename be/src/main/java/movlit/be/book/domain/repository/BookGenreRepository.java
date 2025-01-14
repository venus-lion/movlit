package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookVo;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookGenreRepository {
    List<BookVo> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);
}
