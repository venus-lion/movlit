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
import movlit.be.common.util.ids.BookRCrewId;

@Entity
@Getter
@Setter
@Builder
@Table(name = "book_r_crew")
@NoArgsConstructor
@AllArgsConstructor
public class BookRCrewEntity {

    @EmbeddedId
    private BookRCrewId bookRCrewId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", referencedColumnName = "id")
    private BookcrewEntity bookcrewEntity;
}
