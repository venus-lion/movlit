package movlit.be.testBook.Repository;

import movlit.be.testBook.Entity.BookBestsellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBestsellerRepository extends JpaRepository<BookBestsellerEntity, String> {

}
