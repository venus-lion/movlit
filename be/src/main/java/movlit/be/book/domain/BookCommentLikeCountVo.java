package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookCommentLikeCountVo {

    private Long bookCommentLikeCountId;
    private BookCommentVo bookCommentVo;
    private int count; // 해당 리뷰의 "좋아요"(like) 갯수
    private Long version;

}
