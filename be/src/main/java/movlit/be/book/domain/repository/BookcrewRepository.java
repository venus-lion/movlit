package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.BookcrewVo;

public interface BookcrewRepository {

    List<BookcrewVo> fetchByBook(BookVo bookVo);

}
