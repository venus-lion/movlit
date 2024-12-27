package movlit.be.book.testBook.repository;

import movlit.be.book.testBook.entity.BookRCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRCrewRepository extends JpaRepository<BookRCrewEntity, String> {

}
