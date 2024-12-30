package movlit.be.book.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import movlit.be.common.util.ids.BookNewId;

@Getter
@Setter
@Builder
public class BookNew {
    private BookNewId bookNewId;
    private Book book;
}
