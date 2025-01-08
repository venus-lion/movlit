package movlit.be.book.domain.repository;

import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookCommentRepository {

    BookComment findByBookCommentId(BookCommentId bookCommentId);

    BookComment findByMemberAndBook(Member member, Book book);

    BookComment save(BookComment bookComment);

    void deleteById(BookCommentId bookCommentId);

    double getAverageScoreByBookId(BookId bookId);


    Slice<BookCommentResponseDto> findByBookId(BookId bookId, Pageable pageable);

    Slice<BookCommentResponseDto> findByBookIdAndMemberId(BookId bookId, MemberId memberId, Pageable pageable);
}
