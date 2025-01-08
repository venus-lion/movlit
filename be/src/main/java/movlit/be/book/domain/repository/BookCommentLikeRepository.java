package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.member.domain.Member;

public interface BookCommentLikeRepository {

    // 나의 도서 리뷰 좋아요
    BookCommentLike findByBookCommentAndMember(BookComment bookcomment, Member member);

    BookCommentLike save(BookCommentLike bookCommentLike);

    void delete(BookCommentLike bookCommentLike);


}
