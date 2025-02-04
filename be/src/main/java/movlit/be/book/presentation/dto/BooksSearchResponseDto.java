package movlit.be.book.presentation.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import movlit.be.bookES.BookESVo;

@Data
@Builder
public class BooksSearchResponseDto {

    private List<BookESVo> bookESVoList;
    private long totalPages;

}
