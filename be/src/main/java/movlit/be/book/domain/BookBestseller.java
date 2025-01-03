package movlit.be.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.common.util.ids.BookBestsellerId;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class BookBestseller {

    private BookBestsellerId bookBestsellerId;

    private Book book;

    private Integer bestRank;

    private String bestDuration;
}
