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
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "book_heart_count")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookHeartCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookHeartCountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_heart_id", nullable = false)
    private BookHeartEntity bookHeartEntity;

    @Column(nullable = false)
    private Long count; // 해당 책의 "찜"(heart) 갯수

    @Column
    private Long version;

}
