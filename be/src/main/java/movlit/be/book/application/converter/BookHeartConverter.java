package movlit.be.book.application.converter;

import movlit.be.book.application.converter.BookCommentConverter;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.member.application.converter.MemberConverter;

public class BookHeartConverter {

    private BookHeartConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookHeartEntity toEntity(BookHeart bookHeart) {
        if (bookHeart == null) {
            return null;
        } else {
            return BookHeartEntity.builder()
                    .bookHeartId(bookHeart.getBookHeartId())
                    .bookEntity(BookDetailConverter.toEntity(bookHeart.getBook()))
                    .memberEntity(MemberConverter.toEntity(bookHeart.getMember()))
                    .isHearted(bookHeart.getIsHearted())
                    .build();
        }
    }

    // Entity -> Domain
    public static BookHeart toDomain(BookHeartEntity bookHeartEntity) {
        if (bookHeartEntity == null) {
            return null;
        } else {
            return BookHeart.builder()
                    .bookHeartId(bookHeartEntity.getBookHeartId())
                    .book(BookDetailConverter.toDomain(bookHeartEntity.getBookEntity()))
                    .member(MemberConverter.toDomain(bookHeartEntity.getMemberEntity()))
                    .isHearted(bookHeartEntity.getIsHearted())
                    .build();
        }

    }

}
