package movlit.be.book.presentation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import movlit.be.bookES.BookESDomain;

@Data
@Builder
public class BooksSearchResponseDto {
    private List<BookESDomain> bookESDomainList;
    private long totalPages;
}
