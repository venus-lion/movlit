package movlit.be.book.application.converter;

import movlit.be.book.domain.BookCommentLikeVo;
import movlit.be.book.domain.entity.BookCommentLikeEntity;
import movlit.be.member.application.converter.MemberConverter;

public class BookCommentLikeConverter {
    private BookCommentLikeConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookCommentLikeEntity toEntity(BookCommentLikeVo bookCommentLikeVo) {
        if(bookCommentLikeVo == null)
            return null;
        else
            return BookCommentLikeEntity.builder()
                .id(bookCommentLikeVo.getId())
                .bookCommentEntity(BookCommentConverter.toEntity(bookCommentLikeVo.getBookCommentVo()))
                .bookEntity(BookDetailConverter.toEntity(bookCommentLikeVo.getBookVo()))
                .memberEntity(MemberConverter.toEntity(bookCommentLikeVo.getMember()))
                .isLiked(bookCommentLikeVo.getIsLiked())
                .build();
    }

    // Entity -> Domain
    public static BookCommentLikeVo toDomain(BookCommentLikeEntity bookCommentLikeEntity) {
        if (bookCommentLikeEntity == null)
            return null;
        else
            return BookCommentLikeVo.builder()
                .id(bookCommentLikeEntity.getId())
                .bookCommentVo(BookCommentConverter.toDomain(bookCommentLikeEntity.getBookCommentEntity()))
                .bookVo(BookDetailConverter.toDomain(bookCommentLikeEntity.getBookEntity()))
                .member(MemberConverter.toDomain(bookCommentLikeEntity.getMemberEntity()))
                .isLiked(bookCommentLikeEntity.getIsLiked())
                .build();
    }

}
