package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookNewEntity;
import movlit.be.common.util.ids.BookNewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookNewJpaRepository extends JpaRepository<BookNewEntity, BookNewId> {

}
