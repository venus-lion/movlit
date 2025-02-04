package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookDetailConverter;
import movlit.be.book.application.converter.BookHeartCountConverter;
import movlit.be.book.domain.BookHeartCountVo;
import movlit.be.book.domain.BookVo;
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
    public BookHeartCountVo fetchByBook(BookVo bookVo) {
        BookHeartCountEntity heartCountEntity = bookHeartCountJpaRepository.findByBookEntity(
                        BookDetailConverter.toEntity(
                                bookVo))
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
    public BookHeartCountVo save(BookHeartCountVo bookHeartCountVo) {
        BookHeartCountEntity countEntity = bookHeartCountJpaRepository.save(BookHeartCountConverter.toEntity(
                bookHeartCountVo));
        return BookHeartCountConverter.toDomain(countEntity);
    }

    @Override
    public void increaseHeartCount(BookVo bookVo) {
        bookHeartCountJpaRepository.increaseHeartCount(BookDetailConverter.toEntity(bookVo));
    }

    @Override
    public void decreaseHeartCount(BookVo bookVo) {
        bookHeartCountJpaRepository.decreaseHeartCount(BookDetailConverter.toEntity(bookVo));
    }


}
