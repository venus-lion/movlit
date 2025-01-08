package movlit.be.bookES;



public class BookESConvertor {

    // ES(Document) -> Domain
    public static BookESDomain documentToDomain(BookES bookES){
        return BookESDomain.builder()
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
