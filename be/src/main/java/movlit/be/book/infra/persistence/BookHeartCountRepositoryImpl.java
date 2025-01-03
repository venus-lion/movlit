package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.application.converter.BookHeartConverter;
import movlit.be.book.application.converter.BookHeartCountConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeartCount;
import movlit.be.book.domain.entity.BookHeartCountEntity;
import movlit.be.book.domain.repository.BookHeartCountRepository;
import movlit.be.book.infra.persistence.jpa.BookHeartCountJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookHeartCountRepositoryImpl implements BookHeartCountRepository {
    private final BookHeartCountJpaRepository bookHeartCountJpaRepository;

    @Override
    public BookHeartCount findByBook(Book book) {
        BookHeartCountEntity heartCountEntity =
                bookHeartCountJpaRepository.findByBookEntity(BookConverter.toEntity(book))
                        .orElseThrow();
        return BookHeartCountConverter.toDomain(heartCountEntity);
    }

}
