package movlit.be.bookES;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookESVo {

    private String bookId; // isbn13
    private String isbn; // isbn
    private String title;
    private List<String> crew; // 작가, 편집자, 기타..
    private String publisher;
    private LocalDate pubDate;
    private String description;
    private String categoryName;
    private String bookImgUrl;
    private LocalDate regDt;
    private LocalDate updDt;

}
