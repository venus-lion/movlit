package movlit.be.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookCommentId;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookComment {

    private BookCommentId bookCommentId;
    private BookEntity book;
    private String comment; //  리뷰 - 코멘트
    private BigDecimal score; // 리뷰 - 별점 (not null)
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private String delYn; // 삭제여부 (default : N)

}
