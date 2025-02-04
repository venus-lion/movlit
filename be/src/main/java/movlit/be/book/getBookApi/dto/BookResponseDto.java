package movlit.be.book.getBookApi.dto;

// 신간, 주목할만한 신간 도서 Dto

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

    //    private String version;
    private String logo;
    private String title;
    private String link;
    private String pubDate;

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
        private String isbn13; // bookId
        private String description;
        private String stockStatus;
        private String categoryId;
        private String categoryName; // 대,중,소분류 포함
        private String publisher;
        private boolean adult;

    }

    @Override
    public String toString() {
        return "BookResponseDto 내용{" +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", itemList=" +
                '}';
    }

}
