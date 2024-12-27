package movlit.be.book.detail.domain.repository;

import movlit.be.book.detail.domain.Book;

public interface BookDetailRepository {

    Book findByBookId(String bookId);

}
