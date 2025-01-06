package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.Book;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;

public interface BookRepository {

    Book findByBookId(BookId bookId);

    List<Book> findBooksWithCrewDetails(List<BookId> bookIds);

    List<Book> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);
}
