package movlit.be.book.infra.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookNewConverter;
import movlit.be.book.domain.BookNewVo;
import movlit.be.book.domain.entity.BookNewEntity;
import movlit.be.book.domain.repository.BookNewRepository;
import movlit.be.book.infra.persistence.jpa.BookNewJpaRepository;
import movlit.be.common.exception.BookNewNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookNewRepositoryImpl implements BookNewRepository {

    private final BookNewJpaRepository bookNewJpaRepository;

    @Override
    public List<BookNewVo> findAllBookNew(Pageable pageable) {
        List<BookNewEntity> bookNewEntities = bookNewJpaRepository.findBookNewEntityByPaging(pageable);

        if (bookNewEntities.isEmpty()) {
            throw new BookNewNotFoundException();
        }

        return bookNewEntities.stream()
                .map(bookNewEntity -> BookNewConverter.toDomain(bookNewEntity))
                .collect(Collectors.toList());

    }

}
