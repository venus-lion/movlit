package movlit.be.book.infra.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.dto.BookCommentResponseDto;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentJpaRepository extends JpaRepository<BookCommentEntity, BookCommentId> {


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


    Optional<BookCommentEntity> findByMemberEntityAndBookEntity(MemberEntity memberEntity, BookEntity bookEntity);

    void deleteById(BookCommentId bookCommentId);









}
