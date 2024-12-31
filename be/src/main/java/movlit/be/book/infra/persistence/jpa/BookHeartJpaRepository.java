package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHeartJpaRepository extends JpaRepository<BookHeartEntity, Long> {

   @Query("SELECT COUNT(*) FROM BookHeartEntity l WHERE l.bookEntity = :bookId")
    Optional<Long> countLikesByBookId(@Param("bookId")BookId BookId);



    Optional<BookHeartEntity> findByBookEntityAndMemberEntity(BookEntity book, MemberEntity member);

}
