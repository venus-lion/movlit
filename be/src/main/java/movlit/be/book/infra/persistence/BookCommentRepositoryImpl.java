package movlit.be.book.infra.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.dto.BookCommentsResponseDto;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentJpaRepository;
import movlit.be.common.exception.BookCommentNotFoundException;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCommentRepositoryImpl implements BookCommentRepository {
    private final BookCommentJpaRepository bookCommentJpaRepository;

    @Override
    public BookComment findByBookCommentId(BookCommentId bookCommentId) {
        BookCommentEntity bookCommentEntity = bookCommentJpaRepository.findById(bookCommentId)
                .orElseThrow(BookCommentNotFoundException::new);
        return BookCommentConverter.toDomain(bookCommentEntity);
    }

    @Override
    public BookComment findByMemberAndBook(Member member, Book book) {
        BookCommentEntity bookCommentEntity =
                bookCommentJpaRepository.findByMemberEntityAndBookEntity(MemberConverter.toEntity(member),
                                BookDetailConverter.toEntity(book)).orElse(null);

        return BookCommentConverter.toDomain(bookCommentEntity);
    }

//    @Override
////    public Slice<BookCommentsResponseDto> findByBookId(BookId bookId, Pageable pageable) {
////        Slice<BookCommentsResponseDto> bookCommentEntity = bookCommentJpaRepository.findByBookEntity(bookId, pageable);
////
////        return bookCommentEntity;
////    }

    @Override
    public BookComment save(BookComment bookComment) {
        BookCommentEntity bookCommentEntity = bookCommentJpaRepository.save(BookCommentConverter.toEntity(bookComment));
        return BookCommentConverter.toDomain(bookCommentEntity);
    }




}
