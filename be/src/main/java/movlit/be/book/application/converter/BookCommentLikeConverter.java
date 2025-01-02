package movlit.be.book.application.converter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookCommentLikeId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;

public class BookCommentLikeConverter {
    private BookCommentLikeConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookCommentLikeEntity toEntity(BookCommentLike bookCommentLike) {
        return BookCommentLikeEntity.builder()
                .id(bookCommentLike.getId())
                .bookCommentEntity(BookCommentConverter.toEntity(bookCommentLike.getBookComment()))
                .bookEntity(BookConverter.toEntity(bookCommentLike.getBook()))
                .memberEntity(MemberConverter.toEntity(bookCommentLike.getMember()))
                .isLiked(bookCommentLike.getIsLiked())
                .build();
    }

    // Entity -> Domain
    public static BookCommentLike toDomain(BookCommentLikeEntity bookCommentLikeEntity) {
        return BookCommentLike.builder()
                .id(bookCommentLikeEntity.getId())
                .bookComment(BookCommentConverter.toDomain(bookCommentLikeEntity.getBookCommentEntity()))
                .book(BookConverter.toDomain(bookCommentLikeEntity.getBookEntity()))
                .member(MemberConverter.toDomain(bookCommentLikeEntity.getMemberEntity()))
                .isLiked(bookCommentLikeEntity.getIsLiked())
                .build();
    }

}
