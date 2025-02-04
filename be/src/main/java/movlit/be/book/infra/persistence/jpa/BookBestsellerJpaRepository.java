package movlit.be.book.infra.persistence.jpa;

import java.util.List;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.common.util.ids.BookBestsellerId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookBestsellerJpaRepository extends JpaRepository<BookBestsellerEntity, BookBestsellerId> {

    // @Query("SELECT b FROM BookBestsellerEntity b ORDER BY b.bestRank ASC") -- N+1 문제 발생 예상..
    @Query("SELECT b FROM BookBestsellerEntity b " +
            "JOIN FETCH b.book book " +
            "JOIN FETCH book.bookRCrewEntities brc " +
            "JOIN FETCH brc.bookcrewEntity " +
            "ORDER BY b.bestRank ASC")
    List<BookBestsellerEntity> findBestsellersByPaging(Pageable pageable);

}
