package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.entity.BookNewSpeicialEntity;
import movlit.be.common.util.ids.BookNewSpecialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookNewSpecialJpaRepository extends JpaRepository<BookNewSpeicialEntity, BookNewSpecialId> {

}
