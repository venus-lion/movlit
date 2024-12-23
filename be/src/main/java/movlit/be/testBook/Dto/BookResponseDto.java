package movlit.be.testBook.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private String title;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String isbn;
    private String stockStatus;
    private String categoryName;
    private String publisher;
    private boolean adult;
    private String bestDuration;
    private Integer bestRank;
    private String cover;

}
