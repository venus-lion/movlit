package movlit.be.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.BookId;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class BookGenreIdEntity implements Serializable {

    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "book_id")
    private BookId bookId; // BookEntity의 bookId 자료형이랑 같아야 해서?

}
