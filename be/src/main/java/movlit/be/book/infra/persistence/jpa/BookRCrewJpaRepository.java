package movlit.be.book.infra.persistence.jpa;

import java.util.List;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.common.util.ids.BookRCrewId;
=======
import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.book.domain.entity.BookcrewEntity;
>>>>>>> be/feat/#4-book-detail
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface BookRCrewJpaRepository extends JpaRepository<BookRCrewEntity, BookRCrewId> {



}
