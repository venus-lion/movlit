package movlit.be.book.infra.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookNewSpecialConverter;
import movlit.be.book.domain.BookNewSpecialVo;
import movlit.be.book.domain.entity.BookNewSpecialEntity;
import movlit.be.book.domain.repository.BookNewSpecialRepository;
import movlit.be.book.infra.persistence.jpa.BookNewSpecialJpaRepository;
import movlit.be.common.exception.BookNewSpecialNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookNewSpecialRepositoryImpl implements BookNewSpecialRepository {
    private final BookNewSpecialJpaRepository bookNewSpecialJpaRepository;

    @Override
    public List<BookNewSpecialVo> findAllBookNewSpecial(Pageable pageable) {
        List<BookNewSpecialEntity> bookNewSpecialEntities = bookNewSpecialJpaRepository.findBookNewSpecialEntitiesByPaging(
                pageable);

        if (bookNewSpecialEntities.isEmpty()){
            throw new BookNewSpecialNotFoundException();
        }

        return bookNewSpecialEntities.stream()
                .map(bookNewSpecialEntity -> BookNewSpecialConverter.toDomain(bookNewSpecialEntity))
                .collect(Collectors.toList());
    }

}
