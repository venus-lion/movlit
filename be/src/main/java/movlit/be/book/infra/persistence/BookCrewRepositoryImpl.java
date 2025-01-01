package movlit.be.book.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookcrewConverter;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.book.domain.repository.BookcrewRepository;
import movlit.be.book.infra.persistence.jpa.BookcrewJpaRepository;
import movlit.be.common.exception.BookCrewNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCrewRepositoryImpl implements BookcrewRepository {
    private final BookcrewJpaRepository bookcrewJpaRepository;

    @Override
    public List<Bookcrew> findByBook(Book book) {
        List<BookcrewEntity> bookCrewEntity = bookcrewJpaRepository.findcrewByBookId(book.getBookId());

        return BookcrewConverter.toDomainList(bookCrewEntity);
    }




}
