package movlit.be.book.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import movlit.be.common.util.ids.BookId;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class BookVo {

    private BookId bookId; // isbn13 , uuid
    private String isbn; // isbn
    private String title;
    private String publisher;
    private LocalDateTime pubDate;
    private String description;
    private String categoryName;
    private String bookImgUrl;
    private String stockStatus;
    private String mallUrl;
    private Long heartCount;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private List<BookRCrewVo> bookRCrewVos;
    private List<BookGenreVo> bookGenreVos; // book이 가진 장르들

}
