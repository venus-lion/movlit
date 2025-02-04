package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookCommentLikeConverter;
import movlit.be.book.domain.BookCommentLikeVo;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentLikeJpaRepository;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCommentLikeRepositoryImpl implements BookCommentLikeRepository {

    private final BookCommentLikeJpaRepository bookCommentLikeJpaRepository;

    @Override
    public BookCommentLikeVo fetchByBookCommentAndMember(BookCommentVo bookcomment, Member member) {
        BookCommentLikeEntity bookCommentLikeEntity =
                bookCommentLikeJpaRepository.findByBookCommentEntityAndMemberEntity(
                                BookCommentConverter.toEntity(bookcomment), MemberConverter.toEntity(member))
                        .orElse(null);

        return BookCommentLikeConverter.toDomain(bookCommentLikeEntity);
    }

    @Override
    public BookCommentLikeVo save(BookCommentLikeVo bookCommentLikeVo) {
        BookCommentLikeEntity bookCommentLikeEntity = bookCommentLikeJpaRepository.save(
                BookCommentLikeConverter.toEntity(
                        bookCommentLikeVo));
        return BookCommentLikeConverter.toDomain(bookCommentLikeEntity);
    }

    @Override
    public void delete(BookCommentLikeVo bookCommentLikeVo) {
        bookCommentLikeJpaRepository.delete(BookCommentLikeConverter.toEntity(bookCommentLikeVo));
    }

    @Override
    public void deleteAllByCommentId(BookCommentId bookCommentId) {
        bookCommentLikeJpaRepository.deleteAllByCommentId(bookCommentId);
    }


}
