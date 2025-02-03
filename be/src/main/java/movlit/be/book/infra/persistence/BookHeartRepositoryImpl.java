package movlit.be.book.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.application.converter.BookHeartConverter;
import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.infra.persistence.jpa.BookHeartJpaRepository;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookHeartRepositoryImpl implements BookHeartRepository {
    private final BookHeartJpaRepository bookHeartJpaRepository;

    @Override
    public BookHeartVo fetchByBookAndMember(BookVo bookVo, Member member) {
        BookHeartEntity bookHeartEntity =
                bookHeartJpaRepository.findByBookEntityAndMemberEntity(BookDetailConverter.toEntity(bookVo), MemberConverter.toEntity(member))
                .orElse(null);

        return BookHeartConverter.toDomain(bookHeartEntity);
    }

    // 해당 책을 찜한 멤버 리스트
    @Override
    public List<MemberId> fetchHeartingMembersByBookId(BookId bookId) {
        List<MemberId> HeartingMemberList = bookHeartJpaRepository.findMemberByBookId(bookId)
                .orElseThrow(MemberNotFoundException::new);

        return HeartingMemberList;
    }

    @Override
    public BookHeartVo save(BookHeartVo bookHeartVo) {
        BookHeartEntity bookHeartEntity = bookHeartJpaRepository.save(BookHeartConverter.toEntity(bookHeartVo));
        return BookHeartConverter.toDomain(bookHeartEntity);
    }

    @Override
    public void delete(BookHeartVo bookHeartVo) {
            bookHeartJpaRepository.delete(BookHeartConverter.toEntity(bookHeartVo));

    }

}
