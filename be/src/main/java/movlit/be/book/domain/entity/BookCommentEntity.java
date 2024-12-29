package movlit.be.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookRCrewId;
import movlit.be.common.util.ids.BookcrewId;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
@Table(name = "book_comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentEntity {

    @EmbeddedId
    private BookCommentId bookCommentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @Column(name = "comment")
    private String comment; //  리뷰 - 코멘트

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal score; // 리뷰 - 별점 (not null)

    @CreatedDate
    private LocalDateTime regDt;

    @LastModifiedDate
    private LocalDateTime updDt;

    @ColumnDefault("N")
    private String delYn; // 삭제여부 (default : N)

}
