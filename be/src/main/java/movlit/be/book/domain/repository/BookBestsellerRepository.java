package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookBestsellerVo;
import org.springframework.data.domain.Pageable;

public interface BookBestsellerRepository {

    List<BookBestsellerVo> findAllBestsellers(Pageable pageable);

}
