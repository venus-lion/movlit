package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookHeartCountEntity;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHeartCountJpaRepository extends JpaRepository<BookHeartCountEntity, Long> {

    Optional<BookHeartCountEntity> findByBookEntity(BookEntity bookEntity);

    @Query("SELECT bc.count FROM BookHeartCountEntity bc "
            + "WHERE bc.bookEntity.bookId = :bookId ")
    Optional<Integer> countByBookId(BookId bookId);

    @Modifying
    @Query("UPDATE BookHeartCountEntity bhc SET bhc.count = bhc.count + 1 WHERE bhc.bookEntity = :bookEntity")
    void increaseHeartCount(@Param("bookEntity") BookEntity bookEntity);

    @Modifying
    @Query("UPDATE BookHeartCountEntity bhc SET bhc.count = bhc.count - 1 WHERE bhc.bookEntity = :bookEntity")
    void decreaseHeartCount(@Param("bookEntity") BookEntity bookEntity);


}
