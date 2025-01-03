package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookBestseller;
import org.springframework.data.domain.Pageable;

public interface BookBestsellerRepository {
    List<BookBestseller> findAllBestsellers(Pageable pageable);
}
