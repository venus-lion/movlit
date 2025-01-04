package movlit.be.book.infra.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentLikeConverter;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.application.converter.BookHeartConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.infra.persistence.jpa.BookHeartJpaRepository;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookHeartRepositoryImpl implements BookHeartRepository {
    private final BookHeartJpaRepository bookHeartJpaRepository;

    @Override
    public BookHeart findByBookAndMember(Book book, Member member) {
        BookHeartEntity bookHeartEntity =
                bookHeartJpaRepository.findByBookEntityAndMemberEntity(BookDetailConverter.toEntity(book), MemberConverter.toEntity(member))
                .orElse(null);

        return BookHeartConverter.toDomain(bookHeartEntity);
    }

    @Override
    public Long countHeartsByBookId(BookId bookId) {
        Long heartCount = bookHeartJpaRepository.countHeartsByBookId(bookId).orElse(0L);
        return heartCount;
    }

    @Override
    public BookHeart save(BookHeart bookHeart) {
        BookHeartEntity bookHeartEntity = bookHeartJpaRepository.save(BookHeartConverter.toEntity(bookHeart));
        return BookHeartConverter.toDomain(bookHeartEntity);
    }

    @Override
    public void delete(BookHeart bookHeart) {
        BookHeart existingHeart = findByBookAndMember(bookHeart.getBook(), bookHeart.getMember());
        if(existingHeart != null){
            bookHeartJpaRepository.delete(BookHeartConverter.toEntity(bookHeart));
        }
    }

}
