package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.application.converter.BookHeartCountConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeartCount;
import movlit.be.book.domain.entity.BookHeartCountEntity;
import movlit.be.book.domain.repository.BookHeartCountRepository;
import movlit.be.book.infra.persistence.jpa.BookHeartCountJpaRepository;
import movlit.be.common.util.ids.BookId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookHeartCountRepositoryImpl implements BookHeartCountRepository {
    private final BookHeartCountJpaRepository bookHeartCountJpaRepository;

    @Override
    public BookHeartCount findByBook(Book book) {
        BookHeartCountEntity heartCountEntity = bookHeartCountJpaRepository.findByBookEntity(BookDetailConverter.toEntity(book))
                .orElse(null);
        return BookHeartCountConverter.toDomain(heartCountEntity);
    }

    @Override
    public int countHeartByBookId(BookId bookId) {
        int heartCount =
                bookHeartCountJpaRepository.countByBookId(bookId)
                        .orElse(0);
        return heartCount;
    }

    @Override
    public BookHeartCount save(BookHeartCount bookHeartCount) {
        BookHeartCountEntity countEntity = bookHeartCountJpaRepository.save(BookHeartCountConverter.toEntity(bookHeartCount));
        return BookHeartCountConverter.toDomain(countEntity);
    }


    @Override
    public void increaseHeartCount(Book book) {
          bookHeartCountJpaRepository.increaseHeartCount(BookDetailConverter.toEntity(book));
    }

    @Override
    public void decreaseHeartCount(Book book) {
        bookHeartCountJpaRepository.decreaseHeartCount(BookDetailConverter.toEntity(book));
    }


}
