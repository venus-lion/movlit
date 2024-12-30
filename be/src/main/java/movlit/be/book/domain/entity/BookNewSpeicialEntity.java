package movlit.be.book.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookNewSpecialId;

@Entity
@Getter
@Setter
@Table(name = "book_new_special")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookNewSpeicialEntity {
    @EmbeddedId
    private BookNewSpecialId bookNewSpecialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity bookEntity;

}
