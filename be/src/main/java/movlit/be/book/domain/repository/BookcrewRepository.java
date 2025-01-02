package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.BookcrewId;

public interface BookcrewRepository {

    List<Bookcrew> findByBook(Book book);

}
