package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.common.util.ids.BookCommentLikeId;
import movlit.be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentLikeJpaRepository extends JpaRepository<BookCommentLikeEntity, Long> {

    Optional<BookCommentLikeEntity> findByIdAndMemberEntity(BookComment bookComment, Member member);

}
