package movlit.be.book.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentRequestDto {

    private String bookCommentId;

    @JsonProperty("book_id")
    private String bookId;

    @JsonProperty("member_id")
    private String memberId;

    private String comment; //  리뷰 - 코멘트
    private BigDecimal score; // 리뷰 - 별점 (not null)





}
