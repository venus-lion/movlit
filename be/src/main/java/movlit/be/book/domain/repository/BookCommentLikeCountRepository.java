package movlit.be.book.domain.repository;

import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.BookCommentLikeCount;
import movlit.be.book.domain.BookHeartCount;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;

public interface BookCommentLikeCountRepository {

    BookCommentLikeCount findByBookComment(BookComment bookComment);
    int countLikeByCommentId(BookCommentId bookCommentId);
    void increaseLikeCount(BookComment bookComment);

    void decreaseHeartCount(BookComment bookComment);
    BookCommentLikeCount save(BookCommentLikeCount bookCommentLikeCount);

}
