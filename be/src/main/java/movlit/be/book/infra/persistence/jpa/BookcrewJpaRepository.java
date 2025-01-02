package movlit.be.book.infra.persistence.jpa;

import java.util.List;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.BookcrewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookcrewJpaRepository extends JpaRepository<BookcrewEntity, BookcrewId> {

    @Query("SELECT c FROM BookRCrewEntity r " +
            "LEFT JOIN r.bookcrewEntity c " +
            "WHERE r.book.bookId = :bookId")
    List<BookcrewEntity> findcrewByBookId(@Param("bookId") BookId bookId);

}


