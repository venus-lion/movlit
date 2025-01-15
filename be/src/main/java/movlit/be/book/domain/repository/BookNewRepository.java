package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookNewVo;
import org.springframework.data.domain.Pageable;

public interface BookNewRepository {
    List<BookNewVo> findAllBookNew(Pageable pageable);
}
