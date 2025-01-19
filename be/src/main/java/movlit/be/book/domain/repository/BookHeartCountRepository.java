package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookHeartCountVo;
import movlit.be.book.domain.BookVo;
import movlit.be.common.util.ids.BookId;

public interface BookHeartCountRepository {
    BookHeartCountVo fetchByBook(BookVo bookVo);
    int countHeartByBookId(BookId bookId);
    void increaseHeartCount(BookVo bookVo);

    void decreaseHeartCount(BookVo bookVo);
    BookHeartCountVo save(BookHeartCountVo bookHeartCountVo);


}
