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
import movlit.be.member.domain.entity.MemberEntity;

@Entity
@Getter
@Setter
@Table(name = "book_heart")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookHeartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookHeartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private MemberEntity memberEntity;


}