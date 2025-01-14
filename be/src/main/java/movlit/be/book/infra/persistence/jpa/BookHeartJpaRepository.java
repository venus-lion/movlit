package movlit.be.book.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookHeartJpaRepository extends JpaRepository<BookHeartEntity, Long> {

    Optional<BookHeartEntity> findByBookEntityAndMemberEntity(BookEntity book, MemberEntity member);

}
