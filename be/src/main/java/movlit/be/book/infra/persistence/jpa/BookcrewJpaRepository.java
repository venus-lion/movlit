package movlit.be.book.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookcrewEntity;
<<<<<<< HEAD
=======
import movlit.be.common.util.ids.BookId;
>>>>>>> be/feat/#4-book-detail
import movlit.be.common.util.ids.BookcrewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookcrewJpaRepository extends JpaRepository<BookcrewEntity, BookcrewId> {

    @Query("SELECT c FROM BookRCrewEntity r " +
            "LEFT JOIN r.bookcrewEntity c " +
            "WHERE r.bookEntity.bookId = :bookId")
    List<BookcrewEntity> findcrewByBookId(@Param("bookId") BookId bookId);

}


