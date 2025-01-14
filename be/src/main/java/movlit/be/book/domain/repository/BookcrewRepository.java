package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.Bookcrew;

public interface BookcrewRepository {

    List<Bookcrew> fetchByBook(BookVo bookVo);

}
