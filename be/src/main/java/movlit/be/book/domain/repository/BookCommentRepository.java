package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCommentRepository {

    BookComment save(BookComment bookComment);

//    Page<BookComment> findByBookId(BookId bookId, Pageable pageable);
}
