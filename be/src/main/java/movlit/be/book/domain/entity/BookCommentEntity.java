package movlit.be.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import movlit.be.member.domain.entity.MemberEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(name = "book_comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert  // DefaultValue Insert
@EntityListeners(AuditingEntityListener.class)  // Created/Modifed Date
public class BookCommentEntity {

    @EmbeddedId
    private BookCommentId bookCommentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity memberEntity;

    @Column(name = "comment")
    private String comment; //  리뷰 - 코멘트

    @Column(nullable = false, precision = 2, scale = 1)
    private BigDecimal score; // 리뷰 - 별점 (not null)

    @CreatedDate
    private LocalDateTime regDt;

    @LastModifiedDate
    private LocalDateTime updDt;

    @Column(name = "del_yn", columnDefinition = "bit default 0")
    @ColumnDefault("0")
    private Boolean delYn; // 삭제여부 (default : 0)

}
