package movlit.be.book.domain.repository;

import java.util.List;
import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookVo;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;

public interface BookHeartRepository {

    // 도서 찜
    BookHeartVo fetchByBookAndMember(BookVo bookVo, Member member);

    // 도서 찜한 멤버 리스트
    List<MemberId> fetchHeartingMembersByBookId(BookId bookId);

    BookHeartVo save(BookHeartVo bookHeartVo);

    void delete(BookHeartVo bookHeartVo);

}
