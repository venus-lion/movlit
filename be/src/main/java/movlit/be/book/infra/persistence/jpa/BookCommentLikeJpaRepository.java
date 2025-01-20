package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentLikeJpaRepository extends JpaRepository<BookCommentLikeEntity, Long> {

    Optional<BookCommentLikeEntity> findByBookCommentEntityAndMemberEntity(BookCommentEntity bookCommentEntity, MemberEntity memberEntity);

    @Modifying
    @Query("DELETE FROM BookCommentLikeEntity cl "
            + "WHERE cl.bookCommentEntity.bookCommentId = :bookCommentId")
    void deleteAllByCommentId(@Param("bookCommentId") BookCommentId bookCommentId);



}
