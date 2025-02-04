package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.common.util.ids.BookRCrewId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRCrewJpaRepository extends JpaRepository<BookRCrewEntity, BookRCrewId> {

}
