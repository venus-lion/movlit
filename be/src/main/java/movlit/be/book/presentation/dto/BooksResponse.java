package movlit.be.book.presentation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BooksResponse {

    private List<BookItemDto> books;

    @Data
    @Builder
    public static class BookItemDto {

        private String bookId; // ISBN13
        private String title;
        private List<WriterDto> writers;
        private String pubDate;
        private String bookImgUrl;

        @Data
        @Builder
        public static class WriterDto {

            private String name;
            private String role;

        }

    }

}
