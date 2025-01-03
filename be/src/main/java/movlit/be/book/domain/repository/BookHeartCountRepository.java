package movlit.be.book.domain.repository;

import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeartCount;

public interface BookHeartCountRepository {

    BookHeartCount findByBook(Book book);

}
