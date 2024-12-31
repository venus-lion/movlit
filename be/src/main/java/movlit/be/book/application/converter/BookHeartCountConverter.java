package movlit.be.book.application.converter;

import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.BookHeartCount;
import movlit.be.book.domain.entity.BookHeartCountEntity;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.member.application.converter.MemberConverter;

public class BookHeartCountConverter {
    private BookHeartCountConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }


    // Domain -> Entity
    public static BookHeartCountEntity toEntity(BookHeartCount bookHeartCount) {
        if(bookHeartCount == null)
            return null;
        else
            return BookHeartCountEntity.builder()
                    .bookHeartCountId(bookHeartCount.getBookHeartCountId())
                    .bookHeartEntity(BookHeartConverter.toEntity(bookHeartCount.getBookHeart()))
                    .count(bookHeartCount.getCount())
                    .build();
    }

    // Entity -> Domain
    public static BookHeartCount toDomain(BookHeartCountEntity bookHeartCountEntity) {
        if(bookHeartCountEntity == null)
            return null;
        else
            return BookHeartCount.builder()
                    .bookHeartCountId(bookHeartCountEntity.getBookHeartCountId())
                    .bookHeart(BookHeartConverter.toDomain(bookHeartCountEntity.getBookHeartEntity()))
                    .count(bookHeartCountEntity.getCount())
                    .build();

    }

}
