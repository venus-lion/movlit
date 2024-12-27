package movlit.be.book.infra.persistence.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import movlit.be.book.domain.entity.BookEntity;

@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, String> {
}
