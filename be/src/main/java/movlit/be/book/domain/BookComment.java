package movlit.be.book.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.member.domain.Member;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookComment {

    private BookCommentId bookCommentId;
    private Book book;
    private Member member;
    private String comment; //  리뷰 - 코멘트
    private BigDecimal score; // 리뷰 - 별점 (not null)
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private Boolean delYn; // 삭제여부 (default : 0)



}
