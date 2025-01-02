package movlit.be.book.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookCommentLikeId;
import movlit.be.member.domain.entity.MemberEntity;

@Entity
@Getter
@Setter
@Table(name = "book_comment_like")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_comment_id", referencedColumnName = "id", nullable = false)
    private BookCommentEntity bookCommentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity memberEntity;

    @Column(name = "is_liked")
    private Boolean isLiked; // 도서 리뷰에 대한 좋아요 여부

}
