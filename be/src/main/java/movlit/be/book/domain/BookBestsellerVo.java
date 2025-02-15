package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.common.util.ids.BookBestsellerId;

@NoArgsConstructor
@Getter
@ToString
@Builder
@AllArgsConstructor
public class BookBestsellerVo {

    private BookBestsellerId bookBestsellerId;

    private BookVo bookVo;

    private Integer bestRank;

    private String bestDuration;
}
