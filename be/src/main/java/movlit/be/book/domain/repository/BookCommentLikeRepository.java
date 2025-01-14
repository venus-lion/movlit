package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookCommentLikeVo;
import movlit.be.member.domain.Member;

public interface BookCommentLikeRepository {

    // 나의 도서 리뷰 좋아요
    BookCommentLikeVo fetchByBookCommentAndMember(BookCommentVo bookcomment, Member member);

    BookCommentLikeVo save(BookCommentLikeVo bookCommentLikeVo);

    void delete(BookCommentLikeVo bookCommentLikeVo);




}
