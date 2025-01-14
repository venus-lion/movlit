package movlit.be.book.application.converter;

import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.member.application.converter.MemberConverter;

public class BookCommentConverter {

    private BookCommentConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookCommentEntity toEntity(BookCommentVo bookCommentVo) {
        if(bookCommentVo == null){
            return null;
        }else
            return BookCommentEntity.builder()
                .bookCommentId(bookCommentVo.getBookCommentId())
                .bookEntity(BookDetailConverter.toEntity(bookCommentVo.getBookVo()))
                .memberEntity(MemberConverter.toEntity(bookCommentVo.getMember()))
                .comment(bookCommentVo.getComment())
                .score(bookCommentVo.getScore())
                .regDt(bookCommentVo.getRegDt())
                .updDt(bookCommentVo.getUpdDt())
                .delYn(bookCommentVo.getDelYn())
                .build();
    }

    // Entity -> Domain
    public static BookCommentVo toDomain(BookCommentEntity bookCommentEntity) {
        if(bookCommentEntity == null){
            return null;
        }else
            return BookCommentVo.builder()
                .bookCommentId(bookCommentEntity.getBookCommentId())
                .bookVo(BookDetailConverter.toDomain(bookCommentEntity.getBookEntity()))
                .member(MemberConverter.toDomain(bookCommentEntity.getMemberEntity()))
                .comment(bookCommentEntity.getComment())
                .score(bookCommentEntity.getScore())
                .regDt(bookCommentEntity.getRegDt())
                .updDt(bookCommentEntity.getUpdDt())
                .delYn(bookCommentEntity.getDelYn())
                .build();
    }
}
