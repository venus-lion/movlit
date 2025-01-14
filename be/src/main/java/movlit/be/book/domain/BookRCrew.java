package movlit.be.book.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookRCrewId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookRCrew {

    private BookRCrewId bookRCrewId;
    private BookVo bookVo;
    private Bookcrew bookcrew;

}
