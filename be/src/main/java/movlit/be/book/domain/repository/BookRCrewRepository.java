package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookRCrew;
import movlit.be.book.domain.BookVo;

public interface BookRCrewRepository {


    List<BookRCrew> findByBook(BookVo bookVo);

}
