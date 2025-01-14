package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.member.domain.Member;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookCommentLikeVo {

    private Long id;
    private BookCommentVo bookCommentVo;
    private BookVo bookVo;
    private Member member;
    private Boolean isLiked; // 도서 리뷰에 대한 좋아요 여부

}
