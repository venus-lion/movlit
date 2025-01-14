package movlit.be.book.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import movlit.be.common.util.ids.BookNewSpecialId;

@Getter
@Setter
@Builder
public class BookNewSpecial {
    private BookNewSpecialId bookNewSpecialId;
    private BookVo bookVo;
}
