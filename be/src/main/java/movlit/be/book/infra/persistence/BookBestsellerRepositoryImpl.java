package movlit.be.book.infra.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookBestsellerConverter;
import movlit.be.book.domain.BookBestsellerVo;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.book.domain.repository.BookBestsellerRepository;
import movlit.be.book.infra.persistence.jpa.BookBestsellerJpaRepository;
import movlit.be.common.exception.BookBestsellerNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookBestsellerRepositoryImpl implements BookBestsellerRepository {
    private final BookBestsellerJpaRepository bookBestsellerJpaRepository;

    @Override
    public List<BookBestsellerVo> findAllBestsellers(Pageable pageable) {
        List<BookBestsellerEntity> bestsellerEntities = bookBestsellerJpaRepository.findBestsellersByPaging(pageable);

        if (bestsellerEntities.isEmpty()){
            throw new BookBestsellerNotFoundException();
        }

        return bestsellerEntities.stream()
                .map(bestsellerEntity -> BookBestsellerConverter.toDomain(bestsellerEntity))
                .collect(Collectors.toList());
    }


}
