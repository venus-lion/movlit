package movlit.be.bookES;

import movlit.be.book.presentation.dto.BookRecommendDto;

public class BookESConvertor {

    // ES(Document) -> Domain
    public static BookESVo documentToDomain(BookES bookES) {
        return BookESVo.builder()
                .bookId(bookES.getBookId())
                .isbn(bookES.getIsbn())
                .title(bookES.getTitle())
                .crew(bookES.getCrew())
                .publisher(bookES.getPublisher())
                .categoryName(bookES.getCategoryName())
                .pubDate(bookES.getPubDate())
                .description(bookES.getDescription())
                .bookImgUrl(bookES.getBookImgUrl())
                .regDt(bookES.getRegDt())
                .updDt(bookES.getUpdDt())
                .build();
    }

    // ES(Document) -> RecommendDto
    public static BookRecommendDto documentToRecommendDto(BookES bookES) {
        return BookRecommendDto.builder()
                .bookId(bookES.getBookId())
                .isbn(bookES.getIsbn())
                .title(bookES.getTitle())
                .crew(bookES.getCrew())
                .publisher(bookES.getPublisher())
                .categoryName(bookES.getCategoryName())
                .pubDate(bookES.getPubDate())
                .description(bookES.getDescription())
                .bookImgUrl(bookES.getBookImgUrl())
                .regDt(bookES.getRegDt())
                .updDt(bookES.getUpdDt())
                .build();
    }

}
