package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookNewSpecial;
import org.springframework.data.domain.Pageable;

public interface BookNewSpecialRepository {
    List<BookNewSpecial> findAllBookNewSpecial(Pageable pageable);
}
