package movlit.be.book.infra.persistence;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentJpaRepository;
import movlit.be.common.exception.BookCommentNotFoundException;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
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


//    @Override
//    public Page<BookComment> findByBookId(BookId bookId, Pageable pageable) {
//        Page<BookCommentEntity> bookCommentEntity = bookCommentJpaRepository.findByBookId(bookId, pageable);
//        if (bookCommentEntity == null) {
//            throw new BookCommentNotFoundException();
//        }
//        return (Page<BookComment>) BookCommentConverter.toDomain((BookCommentEntity) bookCommentEntity);
//    }

    @Override
    public BookComment save(BookComment bookComment) {
        BookCommentEntity bookCommentEntity = bookCommentJpaRepository.save(BookCommentConverter.toEntity(bookComment));
        return BookCommentConverter.toDomain(bookCommentEntity);
    }




}
