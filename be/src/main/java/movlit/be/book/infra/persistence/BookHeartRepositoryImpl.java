package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.application.converter.BookHeartConverter;
import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.infra.persistence.jpa.BookHeartJpaRepository;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
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
