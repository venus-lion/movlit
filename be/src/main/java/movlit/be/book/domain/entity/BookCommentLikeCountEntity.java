package movlit.be.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(name = "book_comment_like_count")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentLikeCountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookCommentLikeCountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_comment_id", referencedColumnName = "id", nullable = false)
    private BookCommentEntity bookCommentEntity;

    @Column(nullable = false)
    @ColumnDefault("0") // 기본값을 0으로 설정
    private int count; // 해당 리뷰의 "좋아요"(like) 갯수

    @Column
    private Long version;



}
