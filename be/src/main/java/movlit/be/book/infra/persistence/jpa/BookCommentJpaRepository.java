package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentJpaRepository extends JpaRepository<BookCommentEntity, BookCommentId> {

//    Page<BookCommentEntity> findByBookId(BookId bookId, Pageable pageable);



//    @Query("SELECT NEW movlit.be.book.domain.dto.BookCommentResponseDto("
//            + "    bc.bookCommentId,"
//            + "    bc.score,"
//            + "    bc.comment,"
//            + "    mb.nickname,"
//            + "    mb.profileImgUrl,"
//            + "    false,"
//            + "    lc.count,"
//            + "    (SELECT COUNT(bcc) FROM BookCommentEntity bcc WHERE bcc.bookEntity.bookId = :bookId)"
//            + ")"
//            + "FROM BookCommentEntity bc"
//            + "LEFT JOIN BookCommentLikeCountEntity lc ON lc.bookCommentEntity.bookCommentId = bc.bookCommentId"
//            + "LEFT JOIN MemberEntity mb ON mb.memberId = bc.memberEntity.memberId"
//            + "WHERE bc.bookEntity = :bookId"
//            + "ORDER BY bc.regDt DESC  ")
//    Slice<BookCommentResponseDto> findByBookEntity(@Param("bookId") BookId bookId,
//                                                   @Param("pageable") Pageable pageable);


    //new
//    SELECT
//    new CommentDTO(
//            bc.id,
//            bc.score,
//            bc.comment,
//            COALESCE(bclc.count, 0),
//    m.id,
//    m.nickname,
//    m.profileImgUrl,
//    bc.regDt,
//    bc.updDt
//    )
//    FROM
//    BookComment bc
//    LEFT JOIN
//    bc.bookCommentLikeCount bclc
//    LEFT JOIN
//    bc.book b
//    LEFT JOIN
//    bc.member m
//    WHERE
//    bc.book.id = :bookId
//    ORDER BY
//    bc.updDt DESC



    Optional<BookCommentEntity> findByMemberEntityAndBookEntity(MemberEntity memberEntity, BookEntity bookEntity);

    void deleteById(BookCommentId bookCommentId);









}
