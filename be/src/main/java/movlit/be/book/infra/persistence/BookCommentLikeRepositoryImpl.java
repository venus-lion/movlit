package movlit.be.book.infra.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookCommentLikeConverter;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentLikeJpaRepository;
import movlit.be.common.exception.BookCommentNotFoundException;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCommentLikeRepositoryImpl implements BookCommentLikeRepository {
    private final BookCommentLikeJpaRepository bookCommentLikeJpaRepository;

    @Override
    public BookCommentLike findByBookCommentAndMember(BookComment bookcomment, Member member) {
        BookCommentLikeEntity bookCommentLikeEntity =
                bookCommentLikeJpaRepository.findByBookCommentEntityAndMemberEntity(BookCommentConverter.toEntity(bookcomment), MemberConverter.toEntity(member))
                        .orElse(null);

        return BookCommentLikeConverter.toDomain(bookCommentLikeEntity);
    }

    @Override
    public BookCommentLike save(BookCommentLike bookCommentLike) {
        BookCommentLikeEntity bookCommentLikeEntity = bookCommentLikeJpaRepository.save(BookCommentLikeConverter.toEntity(bookCommentLike));
        return BookCommentLikeConverter.toDomain(bookCommentLikeEntity);
    }

    @Override
    public void delete(BookCommentLike bookCommentLike) {
       bookCommentLikeJpaRepository.delete(BookCommentLikeConverter.toEntity(bookCommentLike));
    }

}
