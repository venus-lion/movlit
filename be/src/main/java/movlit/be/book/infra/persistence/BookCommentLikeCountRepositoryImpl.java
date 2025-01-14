package movlit.be.book.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookCommentLikeCountConverter;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookCommentLikeCountVo;
import movlit.be.book.domain.entity.BookCommentLikeCountEntity;
import movlit.be.book.domain.repository.BookCommentLikeCountRepository;
import movlit.be.book.infra.persistence.jpa.BookCommentLikeCountJpaRepository;
import movlit.be.common.util.ids.BookCommentId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookCommentLikeCountRepositoryImpl implements BookCommentLikeCountRepository {
    private final BookCommentLikeCountJpaRepository bookCommentLikeCountJpaRepository;

    @Override
    public BookCommentLikeCountVo fetchByBookComment(BookCommentVo bookCommentVo) {
        BookCommentLikeCountEntity likeCountEntity = bookCommentLikeCountJpaRepository.findByBookCommentEntity(BookCommentConverter.toEntity(
                        bookCommentVo))
                .orElse(null);

        return BookCommentLikeCountConverter.toDomain(likeCountEntity);
    }

    @Override
    public int countLikeByCommentId(BookCommentId bookCommentId) {
        int likeCount = bookCommentLikeCountJpaRepository.countByBookCommentId(bookCommentId).orElse(0);
        return likeCount;
    }

    @Override
    public void increaseLikeCount(BookCommentVo bookCommentVo) {
        bookCommentLikeCountJpaRepository.increaseLikeCount(BookCommentConverter.toEntity(bookCommentVo));
    }

    @Override
    public void decreaseHeartCount(BookCommentVo bookCommentVo) {
        bookCommentLikeCountJpaRepository.decreaseLikeCount(BookCommentConverter.toEntity(bookCommentVo));

    }

    @Override
    public BookCommentLikeCountVo save(BookCommentLikeCountVo bookCommentLikeCountVo) {
        BookCommentLikeCountEntity likeCountEntity = bookCommentLikeCountJpaRepository.save(BookCommentLikeCountConverter.toEntity(
                bookCommentLikeCountVo));
        return BookCommentLikeCountConverter.toDomain(likeCountEntity);
    }

}
