package movlit.be.book.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.book.domain.Bookcrew;
import movlit.be.common.util.ids.BookId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private BigDecimal averageScore;
    @JsonProperty("heart_count")
    private int heartCount;
    @JsonProperty("is_hearted")
    private boolean isHearted;
    @JsonProperty("book_crew")
    private List<Bookcrew> bookcrewList;

}
