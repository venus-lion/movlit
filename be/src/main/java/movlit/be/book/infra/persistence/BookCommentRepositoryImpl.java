package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentJpaRepository;
import movlit.be.common.exception.BookCommentNotFoundException;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCommentRepositoryImpl implements BookCommentRepository {
    private final BookCommentJpaRepository bookCommentJpaRepository;

    @Override
    public BookCommentVo fetchByBookCommentId(BookCommentId bookCommentId) {
        BookCommentEntity bookCommentEntity = bookCommentJpaRepository.findById(bookCommentId)
                .orElseThrow(BookCommentNotFoundException::new);
        return BookCommentConverter.toDomain(bookCommentEntity);
    }

    @Override
    public BookCommentResponseDto fetchCommentByMemberAndBook(MemberId memberId, BookId bookId) {
        BookCommentResponseDto bookComment =
                bookCommentJpaRepository.fetchCommentByMemberAndBook(memberId, bookId).orElse(null);

        return bookComment;
    }


    @Override
    public Slice<BookCommentResponseDto> fetchByBookId(BookId bookId, Pageable pageable) {
        Slice<BookCommentResponseDto> bookCommentEntity = bookCommentJpaRepository.getCommentsByBookId(bookId, pageable);

        return bookCommentEntity;
    }

    @Override
    public Slice<BookCommentResponseDto> fetchByBookIdAndMemberId(BookId bookId, MemberId memberId, Pageable pageable) {
        Slice<BookCommentResponseDto> bookCommentEntity = bookCommentJpaRepository.getCommentsByBookIdAndMemberId(bookId, memberId, pageable);

        return bookCommentEntity;
    }

    @Override
    public BookCommentVo save(BookCommentVo bookCommentVo) {
        BookCommentEntity bookCommentEntity = bookCommentJpaRepository.save(BookCommentConverter.toEntity(bookCommentVo));
        return BookCommentConverter.toDomain(bookCommentEntity);
    }

    @Override
    public void deleteById(BookCommentId bookCommentId) {
        bookCommentJpaRepository.deleteById(bookCommentId);
    }

    @Override
    public double fetchAverageScoreByBookId(BookId bookId) {
        return bookCommentJpaRepository.getAverageScoreByBookId(bookId).orElse(0.0);
    }


}
