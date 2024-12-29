package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.common.util.ids.BookcrewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookcrewJpaRepository extends JpaRepository<BookcrewEntity, BookcrewId> {

}
