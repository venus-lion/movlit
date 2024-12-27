package movlit.be.book.detail.infra.persistence.jpa;

import movlit.be.book.testBook.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDetailJpaRepository extends JpaRepository<BookEntity, Long> {

    BookEntity  findByBookIdContaining(String bookId);

}
