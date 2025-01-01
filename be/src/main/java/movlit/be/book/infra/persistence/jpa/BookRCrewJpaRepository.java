package movlit.be.book.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.book.domain.entity.BookcrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRCrewJpaRepository extends JpaRepository<BookRCrewEntity, String> {



}
