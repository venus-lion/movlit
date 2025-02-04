package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.BookRCrewId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BookRCrewVo {

    private BookRCrewId bookRCrewId;
    private BookVo bookVo;
    private BookcrewVo bookcrewVo;

}
