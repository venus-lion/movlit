package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookNew;
import org.springframework.data.domain.Pageable;

public interface BookNewRepository {
    List<BookNew> findAllBookNew(Pageable pageable);
}
