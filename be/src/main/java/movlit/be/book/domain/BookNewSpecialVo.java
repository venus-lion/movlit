package movlit.be.book.domain;

import lombok.Builder;
import lombok.Getter;
import movlit.be.common.util.ids.BookNewSpecialId;

@Getter
@Builder
public class BookNewSpecialVo {

    private BookNewSpecialId bookNewSpecialId;
    private BookVo bookVo;

}
