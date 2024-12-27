package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookBestsellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookBestsellerJpaRepository extends JpaRepository<BookBestsellerEntity, String> {

}
