package movlit.be.testBook.Repository;

import movlit.be.testBook.Entity.BookcrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookcrewRepository extends JpaRepository<BookcrewEntity, Long> {

}

