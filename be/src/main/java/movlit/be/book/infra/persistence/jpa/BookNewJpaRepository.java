package movlit.be.book.infra.persistence.jpa;

import java.util.List;
import movlit.be.book.domain.entity.BookNewEntity;
import movlit.be.common.util.ids.BookNewId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookNewJpaRepository extends JpaRepository<BookNewEntity, BookNewId> {

    // 최신출판일을 기준으로 내림차순, 출판일이 동일할 경우 제목을 기준으로 오름차순 정렬
    @Query("SELECT bn FROM BookNewEntity bn " +
            "JOIN FETCH bn.bookEntity bookEntity " +
            "JOIN FETCH bookEntity.bookRCrewEntities brc " +
            "JOIN FETCH brc.bookcrewEntity " +
            "ORDER BY bookEntity.pubDate DESC, bookEntity.title ASC")
    List<BookNewEntity> findBookNewEntityByPaging(Pageable pageable);

}
