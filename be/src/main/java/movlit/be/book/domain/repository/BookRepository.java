package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookVo;
import movlit.be.book.presentation.dto.BookCrewResponseDto;
import movlit.be.book.presentation.dto.BookDetailResponseDto;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface BookRepository {

    BookVo fetchByBookId(BookId bookId);

    List<BookVo> findBooksWithCrewDetails(List<BookId> bookIds);

    List<BookVo> findBooksByGenreIds(List<Long> genreIds, Pageable pageable);

    BookDetailResponseDto fetchBookDetailByBookId(@Param("bookId") BookId bookId, @Param("memberId") MemberId memberId);

    List<BookCrewResponseDto> fetchBookCrewByBookId(BookId bookId);

}
