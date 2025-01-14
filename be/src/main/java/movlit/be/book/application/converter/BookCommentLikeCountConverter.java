package movlit.be.book.application.converter;

import movlit.be.book.domain.BookCommentLikeCountVo;
import movlit.be.book.domain.entity.BookCommentLikeCountEntity;

public class BookCommentLikeCountConverter {
    private BookCommentLikeCountConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookCommentLikeCountEntity toEntity(BookCommentLikeCountVo bookCommentLikeCountVo) {
        if(bookCommentLikeCountVo == null)
            return null;
        else
            return BookCommentLikeCountEntity.builder()
                    .bookCommentLikeCountId(bookCommentLikeCountVo.getBookCommentLikeCountId())
                    .bookCommentEntity(BookCommentConverter.toEntity(bookCommentLikeCountVo.getBookCommentVo()))
                    .count(bookCommentLikeCountVo.getCount())
                    .build();
    }

    // Entity -> Domain
    public static BookCommentLikeCountVo toDomain(BookCommentLikeCountEntity bookCommentLikeCountEntity) {
        if (bookCommentLikeCountEntity == null)
            return null;
        else
            return BookCommentLikeCountVo.builder()
                    .bookCommentLikeCountId(bookCommentLikeCountEntity.getBookCommentLikeCountId())
                    .bookCommentVo(BookCommentConverter.toDomain(bookCommentLikeCountEntity.getBookCommentEntity()))
                    .count(bookCommentLikeCountEntity.getCount())
                    .build();
    }

}
