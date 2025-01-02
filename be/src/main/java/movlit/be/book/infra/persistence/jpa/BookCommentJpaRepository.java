package movlit.be.book.infra.persistence.jpa;

import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentJpaRepository extends JpaRepository<BookCommentEntity, BookCommentId> {
//    Page<BookCommentEntity> findByBookId(BookId bookId, Pageable pageable);




}
