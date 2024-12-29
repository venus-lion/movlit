package movlit.be.book.presentation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import movlit.be.common.util.ids.BookBestsellerId;

@Data
@Builder
public class BookBestsellersResponse {

    private List<BookBestsellerDto> books;
    @Data
    @Builder
    public static class BookBestsellerDto {
        private BookBestsellerId bookId; // ISBN
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
