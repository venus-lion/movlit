package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookVo;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;

public interface BookRepository {

    BookVo fetchByBookId(BookId bookId);

    List<BookVo> findBooksWithCrewDetails(List<BookId> bookIds);

    List<BookVo> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);
}
