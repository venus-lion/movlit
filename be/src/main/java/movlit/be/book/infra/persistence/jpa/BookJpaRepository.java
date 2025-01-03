package movlit.be.book.infra.persistence.jpa;



import java.util.List;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import movlit.be.book.domain.entity.BookEntity;


@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, BookId> {

    @Query("SELECT b FROM BookEntity b "
            + "JOIN FETCH b.bookRCrewEntities brc "
            + "JOIN FETCH brc.bookcrewEntity br "
            + "WHERE br.role = 'AUTHOR'")
    List<BookEntity> findBookEntitiesAndRelated();

}
