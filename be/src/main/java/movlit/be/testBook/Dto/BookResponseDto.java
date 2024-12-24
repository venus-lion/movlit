package movlit.be.testBook.Dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

    private List<Item> item;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String title;
        private String link;
        private String pubDate;
        private String author;
        private String cover;
        private String isbn;
        private String description;
        private String stockStatus;
        private String categoryName;
        private String publisher;
        private boolean adult;
        private String bestDuration;
        private Integer bestRank;

    }
}
