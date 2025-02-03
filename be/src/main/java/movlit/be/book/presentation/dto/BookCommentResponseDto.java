package movlit.be.book.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.MemberId;

@Getter
@Setter
@NoArgsConstructor
public class BookCommentResponseDto {

    private BookCommentId bookCommentId;
    private BigDecimal score;
    private String comment;
    private String nickname;
    private String profileImgUrl;
    private boolean isLiked; // 기본값 false
    private int likeCount; // 해당 리뷰 카운트
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private Long allCommentsCount;
    private MemberId memberId;

    public BookCommentResponseDto(BookCommentId bookCommentId, BigDecimal score, String comment, String nickname,
                                  String profileImgUrl, boolean isLiked, int likeCount, LocalDateTime regDt,
                                  LocalDateTime updDt, Long allCommentsCount, MemberId memberId) {
        this.bookCommentId = bookCommentId;
        this.score = score;
        this.comment = comment;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
        this.regDt = regDt;
        this.updDt = updDt;
        this.allCommentsCount = allCommentsCount;
        this.memberId = memberId;
    }

}
