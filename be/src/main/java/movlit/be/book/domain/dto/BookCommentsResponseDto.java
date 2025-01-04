package movlit.be.book.domain.dto;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookCommentId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCommentsResponseDto {
    private BookCommentId bookCommentId;
    private BigDecimal score;
    private String comment;
    private String nickname;
    private String profileImgUrl;
    private Long likeCount; // 서브 쿼리 결과
    private boolean isLiked; // 기본값 false


}
