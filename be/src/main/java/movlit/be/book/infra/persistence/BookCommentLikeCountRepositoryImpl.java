package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookCommentLikeCountConverter;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLikeCount;
import movlit.be.book.domain.entity.BookCommentLikeCountEntity;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.book.domain.repository.BookCommentLikeCountRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentLikeCountJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentLikeJpaRepository;
import movlit.be.common.util.ids.BookCommentId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCommentLikeCountRepositoryImpl implements BookCommentLikeCountRepository {
    private final BookCommentLikeCountJpaRepository bookCommentLikeCountJpaRepository;

    @Override
    public BookCommentLikeCount findByBookComment(BookComment bookComment) {
        BookCommentLikeCountEntity likeCountEntity = bookCommentLikeCountJpaRepository.findByBookCommentEntity(BookCommentConverter.toEntity(bookComment))
                .orElse(null);

        return BookCommentLikeCountConverter.toDomain(likeCountEntity);
    }

    @Override
    public int countLikeByCommentId(BookCommentId bookCommentId) {
        int likeCount = bookCommentLikeCountJpaRepository.countByBookCommentId(bookCommentId).orElse(0);
        return likeCount;
    }

    @Override
    public void increaseLikeCount(BookComment bookComment) {
        bookCommentLikeCountJpaRepository.increaseLikeCount(BookCommentConverter.toEntity(bookComment));
    }

    @Override
    public void decreaseHeartCount(BookComment bookComment) {
        bookCommentLikeCountJpaRepository.decreaseLikeCount(BookCommentConverter.toEntity(bookComment));

    }

    @Override
    public BookCommentLikeCount save(BookCommentLikeCount bookCommentLikeCount) {
        BookCommentLikeCountEntity likeCountEntity = bookCommentLikeCountJpaRepository.save(BookCommentLikeCountConverter.toEntity(bookCommentLikeCount));
        return BookCommentLikeCountConverter.toDomain(likeCountEntity);
    }

}
