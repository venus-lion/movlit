package movlit.be.book.infra.persistence.jpa;



import java.util.List;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import movlit.be.book.domain.entity.BookEntity;


@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, BookId> {
    @Query("SELECT b FROM BookEntity b "
            + "JOIN FETCH b.bookRCrewEntities brc "
            + "JOIN FETCH brc.bookcrewEntity br "
            + "WHERE br.role = 'AUTHOR'")
    List<BookEntity> findBookEntitiesAndRelated();

    @Query("SELECT DISTINCT b FROM BookEntity b "
            + "JOIN FETCH b.bookRCrewEntities brc "
            + "JOIN FETCH brc.bookcrewEntity br "
            + "WHERE b.bookId IN :bookIds")
    List<BookEntity> findBooksWithCrewDetails(@Param("bookIds") List<BookId> bookIds);


    @Query("SELECT DISTINCT b "
            + "FROM BookEntity b "
            + "LEFT JOIN b.bookGenreEntities bg " // LEFT JOIN으로 변경 -- oneToMany라서 페이징과 컬렉션조회 같이 안되서 그냥 join -> 그래서 yml에 batchsize 설정
            + "JOIN FETCH b.bookRCrewEntities br " // BookEntity와 BookRCrewEntities가 oneToMany지만 distinct 덕분에 가능
            + "JOIN FETCH br.bookcrewEntity bc " // ToOne은 패치조인 가능
            + "WHERE bg.bookGenreIdEntity.genreId IN :genreIds")
    List<BookEntity> findBooksByGenreIds(@Param("genreIds") List<Long> genreIds, Pageable pageable);

}
