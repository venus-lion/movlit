package movlit.be.book.domain.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class BookCommentResponseDto {
    private BookCommentId bookCommentId;
    private BigDecimal score;
    private String comment;
    private String nickname;
    private String profileImgUrl;
    private boolean isLiked; // 기본값 false
    private Long likeCount; // 해당 리뷰 카운트
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private Long allCommentsCount;


}
