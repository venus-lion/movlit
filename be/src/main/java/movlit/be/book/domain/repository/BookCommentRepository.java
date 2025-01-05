package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookCommentRepository {

    BookComment findByBookCommentId(BookCommentId bookCommentId);

    BookComment findByMemberAndBook(Member member, Book book);

    BookComment save(BookComment bookComment);

    void deleteById(BookCommentId bookCommentId);


//    Slice<BookCommentsResponseDto> findByBookId(BookId bookId, Pageable pageable);
}
