package movlit.be.book.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.common.util.ids.BookId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookDetailResponseDto {

    @JsonProperty("book_id")
    private BookId bookId; // isbn13 , uuid
    private String isbn; // isbn
    private String title;
    private String publisher;

    @JsonProperty("pub_date")
    private LocalDateTime pubDate;
    private String description;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("book_img_url")
    private String bookImgUrl;

    @JsonProperty("stock_status")
    private String stockStatus;

    @JsonProperty("mall_url")
    private String mallUrl;

    @JsonProperty("average_score")
    private Double averageScore;

    @JsonProperty("heart_count")
    private int heartCount;

    @JsonProperty("is_hearted")
    private boolean isHearted;

    @JsonProperty("book_crew")

    private List<BookCrewResponseDto> bookcrewList = new ArrayList<>();

    public BookDetailResponseDto(BookId bookId, String isbn, String title, String publisher, LocalDateTime pubDate,
                                 String description,
                                 String categoryName, String bookImgUrl, String stockStatus, String mallUrl,
                                 Double averageScore,
                                 int heartCount, boolean isHearted) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.description = description;
        this.categoryName = categoryName;
        this.bookImgUrl = bookImgUrl;
        this.stockStatus = stockStatus;
        this.mallUrl = mallUrl;
        this.averageScore = averageScore;
        this.heartCount = heartCount;
        this.isHearted = isHearted;
    }

    public void setBookcrewList(List<BookCrewResponseDto> bookcrewList) {
        this.bookcrewList = bookcrewList;
    }


}
