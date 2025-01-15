package movlit.be.book.domain;

import lombok.Builder;
import lombok.Getter;
import movlit.be.common.util.ids.BookNewId;

@Getter
@Builder
public class BookNewVo {
    private BookNewId bookNewId;
    private BookVo bookVo;
}
