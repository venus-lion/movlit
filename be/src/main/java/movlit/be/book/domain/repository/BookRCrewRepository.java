package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookRCrewVo;
import movlit.be.book.domain.BookVo;

public interface BookRCrewRepository {


    List<BookRCrewVo> findByBook(BookVo bookVo);

}
