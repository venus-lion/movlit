package movlit.be.book.testBook.repository;

import movlit.be.book.testBook.entity.BookcrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookcrewRepository extends JpaRepository<BookcrewEntity, String> {

}
