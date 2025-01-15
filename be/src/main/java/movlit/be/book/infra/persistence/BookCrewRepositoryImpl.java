package movlit.be.book.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookcrewConverter;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.BookcrewVo;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.book.domain.repository.BookcrewRepository;
import movlit.be.book.infra.persistence.jpa.BookcrewJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCrewRepositoryImpl implements BookcrewRepository {
    private final BookcrewJpaRepository bookcrewJpaRepository;

    @Override
    public List<BookcrewVo> fetchByBook(BookVo bookVo) {
        List<BookcrewEntity> bookCrewEntity = bookcrewJpaRepository.findcrewByBookId(bookVo.getBookId());

        return BookcrewConverter.toDomainList(bookCrewEntity);
    }




}
