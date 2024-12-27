package movlit.be.book.testBook.repository;

import movlit.be.book.testBook.entity.BookBestsellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBestsellerRepository extends JpaRepository<BookBestsellerEntity, String> {

}
