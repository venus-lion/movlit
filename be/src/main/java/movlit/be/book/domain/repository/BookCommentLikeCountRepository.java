package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookCommentLikeCountVo;
import movlit.be.common.util.ids.BookCommentId;

public interface BookCommentLikeCountRepository {

    BookCommentLikeCountVo fetchByBookComment(BookCommentVo bookCommentVo);
    int countLikeByCommentId(BookCommentId bookCommentId);
    void increaseLikeCount(BookCommentVo bookCommentVo);

    void decreaseHeartCount(BookCommentVo bookCommentVo);
    BookCommentLikeCountVo save(BookCommentLikeCountVo bookCommentLikeCountVo);

    void deleteAllByCommentId(BookCommentId bookCommentId);

}
