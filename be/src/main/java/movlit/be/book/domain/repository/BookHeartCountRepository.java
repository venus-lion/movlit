package movlit.be.book.domain.repository;

import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeartCount;
import movlit.be.common.util.ids.BookId;

public interface BookHeartCountRepository {
    BookHeartCount findByBook(Book book);
    int countHeartByBookId(BookId bookId);
    void increaseHeartCount(Book book);

    void decreaseHeartCount(Book book);
    BookHeartCount save(BookHeartCount bookHeartCount);

}
