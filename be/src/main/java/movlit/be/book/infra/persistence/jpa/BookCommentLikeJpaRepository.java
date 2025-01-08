package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.common.util.ids.BookCommentLikeId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentLikeJpaRepository extends JpaRepository<BookCommentLikeEntity, Long> {

    Optional<BookCommentLikeEntity> findByBookCommentEntityAndMemberEntity(BookCommentEntity bookCommentEntity, MemberEntity memberEntity);



}
