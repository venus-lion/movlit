package movlit.be.book.domain.repository;

import movlit.be.book.domain.Book;
import movlit.be.common.util.ids.BookId;

public interface BookRepository {

    Book findByBookId(BookId bookId);


}
