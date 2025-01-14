package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookCommentLikeCountEntity;
import movlit.be.common.util.ids.BookCommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentLikeCountJpaRepository extends JpaRepository<BookCommentLikeCountEntity, Long> {

    Optional<BookCommentLikeCountEntity> findByBookCommentEntity(BookCommentEntity bookCommentEntity);

    @Query("SELECT lc.count FROM BookCommentLikeCountEntity lc "
            + "WHERE lc.bookCommentEntity.bookCommentId = :bookCommentId")
    Optional<Integer> countByBookCommentId(BookCommentId bookCommentId);

    @Modifying
    @Query("UPDATE BookCommentLikeCountEntity blc SET blc.count = blc.count + 1 WHERE blc.bookCommentEntity = :bookCommentEntity")
    void increaseLikeCount(@Param("bookCommentEntity") BookCommentEntity bookCommentEntity);

    @Modifying
    @Query("UPDATE BookCommentLikeCountEntity blc SET blc.count = blc.count - 1 WHERE blc.bookCommentEntity = :bookCommentEntity")
    void decreaseLikeCount(@Param("bookCommentEntity") BookCommentEntity bookCommentEntity);

}
