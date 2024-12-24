package movlit.be.testBook.Repository;

import movlit.be.testBook.Entity.BookRCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRCrewRepository extends JpaRepository<BookRCrewEntity, Long> {

}
