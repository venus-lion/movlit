package movlit.be.book.application.converter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookCommentId;

public class BookCommentConverter {

    private BookCommentConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    private BookCommentId bookCommentId;
    private BookEntity book;
    private String comment; //  리뷰 - 코멘트
    private BigDecimal score; // 리뷰 - 별점 (not null)
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private String delYn; // 삭제여부 (default : N)


    // Domain -> Entity
    public static BookCommentEntity toEntity(BookComment bookComment) {
        return BookCommentEntity.builder()
                .bookCommentId(bookComment.getBookCommentId())
                .book(bookComment.getBook())
                .comment(bookComment.getComment())
                .score(bookComment.getScore())
                .regDt(bookComment.getRegDt())
                .updDt(bookComment.getUpdDt())
                .delYn(bookComment.getDelYn())                
                .build();
    }

    // Entity -> Domain
    public static BookComment toDomain(BookCommentEntity bookCommentEntity) {
        return BookComment.builder()
                .bookCommentId(bookCommentEntity.getBookCommentId())
                .book(bookCommentEntity.getBook())
                .comment(bookCommentEntity.getComment())
                .score(bookCommentEntity.getScore())
                .regDt(bookCommentEntity.getRegDt())
                .updDt(bookCommentEntity.getUpdDt())
                .delYn(bookCommentEntity.getDelYn())
                .build();
    }
}
