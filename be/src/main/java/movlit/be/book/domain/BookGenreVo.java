package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BookGenreVo {

    private Long genreId;
    private BookVo bookVo; // 해당 genreId에 매핑되는 Book 도메인

}
