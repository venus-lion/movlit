package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookRCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRCrewJpaRepository extends JpaRepository<BookRCrewEntity, String> {

}
