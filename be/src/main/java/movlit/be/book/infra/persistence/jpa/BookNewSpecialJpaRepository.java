package movlit.be.book.infra.persistence.jpa;


import java.util.List;
import movlit.be.book.domain.entity.BookNewSpecialEntity;
import movlit.be.common.util.ids.BookNewSpecialId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookNewSpecialJpaRepository extends JpaRepository<BookNewSpecialEntity, BookNewSpecialId> {
    // 최신출판일을 기준으로 내림차순
    @Query("SELECT bns FROM BookNewSpecialEntity bns " +
    "JOIN FETCH bns.bookEntity bookEntity " +
    "JOIN FETCH bookEntity.bookRCrewEntities brc " +
    "JOIN FETCH brc.bookcrewEntity " +
    "ORDER BY bookEntity.pubDate DESC")
    List<BookNewSpecialEntity> findBookNewSpecialEntitiesByPaging(Pageable pageable);
}
