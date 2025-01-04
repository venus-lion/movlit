package movlit.be.book.infra.persistence.jpa;

import org.springframework.data.domain.Pageable;
import java.util.List;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookGenreEntity;
import movlit.be.book.domain.entity.BookGenreIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookGenreJpaRepository extends JpaRepository<BookGenreEntity, BookGenreIdEntity> {
    // genreIds 리스트에 해당하는 BookEntity list를 반환하는 쿼리
    @Query("SELECT DISTINCT bg.bookEntity FROM BookGenreEntity bg WHERE bg.bookGenreIdEntity.genreId IN :genreIds")
    List<BookEntity> findBooksByGenreIds(@Param("genreIds") List<Long> genreIds, Pageable pageable);
}
