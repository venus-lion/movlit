package movlit.be.book.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.common.util.ids.BookId;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Book {

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

}
