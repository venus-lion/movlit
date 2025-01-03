package movlit.be.book.application.converter;

import movlit.be.book.domain.BookBestseller;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.book.domain.entity.BookBestsellerEntity;
public class BookBestsellerConverter {

    private BookBestsellerConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookBestsellerEntity toEntity(BookBestseller bookBestseller) {
        return BookBestsellerEntity.builder()
                .bookBestsellerId(bookBestseller.getBookBestsellerId())
                .book(BookConverter.toEntity(bookBestseller.getBook())) // bookentity로
                .bestRank(bookBestseller.getBestRank())
                .bestDuration(bookBestseller.getBestDuration())
                .build();
    }

    // Entity -> Domain
    public static BookBestseller toDomain(BookBestsellerEntity bookBestsellerEntity) {
        return BookBestseller.builder()
                .bookBestsellerId(bookBestsellerEntity.getBookBestsellerId())
                .book(BookConverter.toDomain(bookBestsellerEntity.getBook()))
                .bestRank(bookBestsellerEntity.getBestRank())
                .bestDuration(bookBestsellerEntity.getBestDuration())
                .build();
    }
}
