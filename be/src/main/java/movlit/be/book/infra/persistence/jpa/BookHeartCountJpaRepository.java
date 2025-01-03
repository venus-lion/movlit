package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookHeartCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHeartCountJpaRepository extends JpaRepository<BookHeartCountEntity, Long> {

    Optional<BookHeartCountEntity> findByBookEntity(BookEntity bookEntity);

}
