package movlit.be.book.domain.repository;

import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookVo;
import movlit.be.member.domain.Member;

public interface BookHeartRepository {

    // 도서 찜
    BookHeartVo fetchByBookAndMember(BookVo bookVo, Member member);


    BookHeartVo save(BookHeartVo bookHeartVo);

    void delete(BookHeartVo bookHeartVo);

}
