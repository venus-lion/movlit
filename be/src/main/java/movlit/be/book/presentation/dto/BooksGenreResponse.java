package movlit.be.book.presentation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BooksGenreResponse {

    private List<BookItemWithGenreDto> bookWithGenres;
    @Data
    @Builder
    public static class BookItemWithGenreDto {
        private String bookId; // ISBN13
        private String title;
        private List<WriterDto> writers;
        private List<GenreDto> genres;
        private String pubDate;
        private String bookImgUrl;

        @Data
        @Builder
        public static class WriterDto {
            private String name;
            private String role;
        }

        @Data
        @Builder
        public static class GenreDto {
            private Long genreId;
            private String genreName;
        }
    }
}
