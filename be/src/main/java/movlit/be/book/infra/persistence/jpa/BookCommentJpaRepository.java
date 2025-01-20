package movlit.be.book.infra.persistence.jpa;


import java.util.Optional;

import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentJpaRepository extends JpaRepository<BookCommentEntity, BookCommentId> {



    @Query("SELECT NEW movlit.be.book.presentation.dto.BookCommentResponseDto( "
            + "bc.bookCommentId, bc.score, bc.comment, "
            + "m.nickname, m.profileImgUrl , false, "
            + "IFNULL(bclc.count, 0) AS likeCount , bc.regDt, bc.updDt, "
            + "(SELECT COUNT(bcc) FROM BookCommentEntity bcc WHERE bcc.bookEntity.bookId = :bookId ) AS allCommentsCount"
            + ") "
            + "FROM BookCommentEntity bc "
            + "LEFT JOIN BookCommentLikeCountEntity bclc ON bclc.bookCommentEntity.bookCommentId = bc.bookCommentId "
            + "LEFT JOIN MemberEntity m ON m.memberId = bc.memberEntity.memberId "
            + "WHERE bc.bookEntity.bookId = :bookId "
            + "order by bc.regDt DESC "
    )
    Slice<BookCommentResponseDto> getCommentsByBookId(@Param("bookId") BookId bookId,
                                                  @Param("pageable") Pageable pageable);

    @Query("SELECT NEW movlit.be.book.presentation.dto.BookCommentResponseDto( "
            + "bc.bookCommentId, bc.score, bc.comment, "
            + "m.nickname, m.profileImgUrl , COALESCE(cl.isLiked, false) AS isLiked, "
            + "COALESCE(bclc.count, 0) AS likeCount , bc.regDt, bc.updDt, "
            + "(SELECT COUNT(bcc) FROM BookCommentEntity bcc WHERE bcc.bookEntity.bookId = :bookId ) AS allCommentsCount"
            + ") "
            + "FROM BookCommentEntity bc "
            + "LEFT JOIN BookCommentLikeCountEntity bclc ON bclc.bookCommentEntity.bookCommentId = bc.bookCommentId "
            + "LEFT JOIN MemberEntity m ON m.memberId = bc.memberEntity.memberId "
            + "LEFT JOIN BookCommentLikeEntity cl ON cl.bookCommentEntity.bookCommentId = bc.bookCommentId AND cl.memberEntity.memberId = :memberId "
            + "WHERE bc.bookEntity.bookId = :bookId "
            + "order by bc.regDt DESC "
    )
    Slice<BookCommentResponseDto> getCommentsByBookIdAndMemberId(@Param("bookId") BookId bookId, @Param("memberId") MemberId memberId,
                                                      @Param("pageable") Pageable pageable);

    @Query("SELECT NEW movlit.be.book.presentation.dto.BookCommentResponseDto( "
            + "bc.bookCommentId, "
            + "bc.score,"
            + "bc.comment, "
            + "m.nickname, "
            + "m.profileImgUrl, "
            + "false AS isLiked, "
            + "0 AS likeCount, "
            + "bc.regDt, "
            + "bc.updDt,"
            + "0L AS allCommentsCount"
            + " ) "
            + "FROM BookCommentEntity bc "
            + "LEFT JOIN MemberEntity m on m.memberId = bc.memberEntity.memberId "
            + "WHERE bc.bookEntity.bookId = :bookId and m.memberId = :memberId")
    Optional<BookCommentResponseDto> fetchCommentByMemberAndBook(@Param("memberId") MemberId memberId, @Param("bookId") BookId bookId);



    void deleteById(BookCommentId bookCommentId);

    @Query("SELECT AVG(bc.score) FROM BookCommentEntity bc where bc.bookEntity.bookId= :bookId")
    Optional<Double> getAverageScoreByBookId(@Param("bookId") BookId bookId);
}
