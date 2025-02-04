package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookNewSpecialVo;
import org.springframework.data.domain.Pageable;

public interface BookNewSpecialRepository {

    List<BookNewSpecialVo> findAllBookNewSpecial(Pageable pageable);

}
