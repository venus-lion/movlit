package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookGenreJpaRepository extends JpaRepository<BookGenreEntity, Long> {

}
