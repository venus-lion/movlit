package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookcrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookcrewJpaRepository extends JpaRepository<BookcrewEntity, String> {

}
