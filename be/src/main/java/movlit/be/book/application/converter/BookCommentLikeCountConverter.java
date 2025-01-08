package movlit.be.book.application.converter;

import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.BookCommentLikeCount;
import movlit.be.book.domain.entity.BookCommentLikeCountEntity;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.member.application.converter.MemberConverter;

public class BookCommentLikeCountConverter {
    private BookCommentLikeCountConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookCommentLikeCountEntity toEntity(BookCommentLikeCount bookCommentLikeCount) {
        if(bookCommentLikeCount == null)
            return null;
        else
            return BookCommentLikeCountEntity.builder()
                    .bookCommentLikeCountId(bookCommentLikeCount.getBookCommentLikeCountId())
                    .bookCommentEntity(BookCommentConverter.toEntity(bookCommentLikeCount.getBookComment()))
                    .count(bookCommentLikeCount.getCount())
                    .build();
    }

    // Entity -> Domain
    public static BookCommentLikeCount toDomain(BookCommentLikeCountEntity bookCommentLikeCountEntity) {
        if (bookCommentLikeCountEntity == null)
            return null;
        else
            return BookCommentLikeCount.builder()
                    .bookCommentLikeCountId(bookCommentLikeCountEntity.getBookCommentLikeCountId())
                    .bookComment(BookCommentConverter.toDomain(bookCommentLikeCountEntity.getBookCommentEntity()))
                    .count(bookCommentLikeCountEntity.getCount())
                    .build();
    }

}
