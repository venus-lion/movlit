package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookRCrew;
import movlit.be.book.domain.Bookcrew;

public interface BookRCrewRepository {


    List<BookRCrew> findByBook(Book book);

}
