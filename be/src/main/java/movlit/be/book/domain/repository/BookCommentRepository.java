package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BookCommentRepository {

    BookCommentVo fetchByBookCommentId(BookCommentId bookCommentId);

    BookCommentResponseDto fetchCommentByMemberAndBook(MemberId memberId, BookId bookId);

    BookCommentVo save(BookCommentVo bookCommentVo);

    void deleteById(BookCommentId bookCommentId);

    double fetchAverageScoreByBookId(BookId bookId);

    Slice<BookCommentResponseDto> fetchByBookId(BookId bookId, Pageable pageable);

    Slice<BookCommentResponseDto> fetchByBookIdAndMemberId(BookId bookId, MemberId memberId, Pageable pageable);

}
