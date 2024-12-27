package movlit.be.book.testBook.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import movlit.be.book.testBook.entity.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
}
