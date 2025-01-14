package movlit.be.book.application.converter;

import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.entity.BookHeartEntity;
import movlit.be.member.application.converter.MemberConverter;

public class BookHeartConverter {

    private BookHeartConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookHeartEntity toEntity(BookHeartVo bookHeartVo) {
        if (bookHeartVo == null) {
            return null;
        } else {
            return BookHeartEntity.builder()
                    .bookHeartId(bookHeartVo.getBookHeartId())
                    .bookEntity(BookDetailConverter.toEntity(bookHeartVo.getBookVo()))
                    .memberEntity(MemberConverter.toEntity(bookHeartVo.getMember()))
                    .isHearted(bookHeartVo.getIsHearted())
                    .build();
        }
    }

    // Entity -> Domain
    public static BookHeartVo toDomain(BookHeartEntity bookHeartEntity) {
        if (bookHeartEntity == null) {
            return null;
        } else {
            return BookHeartVo.builder()
                    .bookHeartId(bookHeartEntity.getBookHeartId())
                    .bookVo(BookDetailConverter.toDomain(bookHeartEntity.getBookEntity()))
                    .member(MemberConverter.toDomain(bookHeartEntity.getMemberEntity()))
                    .isHearted(bookHeartEntity.getIsHearted())
                    .build();
        }

    }

}
