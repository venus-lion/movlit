package movlit.be.book.domain.repository;

import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.BookHeart;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import org.springframework.data.repository.query.Param;

public interface BookHeartRepository {

    // 도서 찜
    BookHeart findByBookAndMember(Book book, Member member);


    BookHeart save(BookHeart bookHeart);

    void delete(BookHeart bookHeart);

}
