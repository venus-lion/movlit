package movlit.be.book.testBook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.book.testBook.entity.BookEntity;
import movlit.be.common.util.ids.BookBestsellerId;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Table(name = "book_best_seller")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookBestsellerEntity {
    @EmbeddedId
    private BookBestsellerId bookBestsellerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Column(name = "best_rank")
    private Integer bestRank;

    @Column(name = "best_duration")
    private String bestDuration;

}
