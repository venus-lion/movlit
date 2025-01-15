package movlit.be.book.application.converter;

import movlit.be.book.domain.BookBestsellerVo;
import movlit.be.book.domain.entity.BookBestsellerEntity;
public class BookBestsellerConverter {

    private BookBestsellerConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookBestsellerEntity toEntity(BookBestsellerVo bookBestsellerVo) {
        return BookBestsellerEntity.builder()
                .bookBestsellerId(bookBestsellerVo.getBookBestsellerId())
                .book(BookConverter.toEntity(bookBestsellerVo.getBookVo())) // bookentity로
                .bestRank(bookBestsellerVo.getBestRank())
                .bestDuration(bookBestsellerVo.getBestDuration())
                .build();
    }

    // Entity -> Domain
    public static BookBestsellerVo toDomain(BookBestsellerEntity bookBestsellerEntity) {
        return BookBestsellerVo.builder()
                .bookBestsellerId(bookBestsellerEntity.getBookBestsellerId())
                .bookVo(BookConverter.toDomain(bookBestsellerEntity.getBook()))
                .bestRank(bookBestsellerEntity.getBestRank())
                .bestDuration(bookBestsellerEntity.getBestDuration())
                .build();
    }
}
