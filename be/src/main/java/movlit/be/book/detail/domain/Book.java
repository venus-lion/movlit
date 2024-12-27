package movlit.be.book.detail.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Book {
    private String bookId; // isbn13 , uuid
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
