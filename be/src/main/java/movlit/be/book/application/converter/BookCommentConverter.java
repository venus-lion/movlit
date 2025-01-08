package movlit.be.book.application.converter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.member.application.converter.MemberConverter;

public class BookCommentConverter {

    private BookCommentConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookCommentEntity toEntity(BookComment bookComment) {
        if(bookComment == null){
            return null;
        }else
            return BookCommentEntity.builder()
                .bookCommentId(bookComment.getBookCommentId())
                .bookEntity(BookDetailConverter.toEntity(bookComment.getBook()))
                .memberEntity(MemberConverter.toEntity(bookComment.getMember()))
                .comment(bookComment.getComment())
                .score(bookComment.getScore())
                .regDt(bookComment.getRegDt())
                .updDt(bookComment.getUpdDt())
                .delYn(bookComment.getDelYn())                
                .build();
    }

    // Entity -> Domain
    public static BookComment toDomain(BookCommentEntity bookCommentEntity) {
        if(bookCommentEntity == null){
            return null;
        }else
            return BookComment.builder()
                .bookCommentId(bookCommentEntity.getBookCommentId())
                .book(BookDetailConverter.toDomain(bookCommentEntity.getBookEntity()))
                .member(MemberConverter.toDomain(bookCommentEntity.getMemberEntity()))
                .comment(bookCommentEntity.getComment())
                .score(bookCommentEntity.getScore())
                .regDt(bookCommentEntity.getRegDt())
                .updDt(bookCommentEntity.getUpdDt())
                .delYn(bookCommentEntity.getDelYn())
                .build();
    }
}
