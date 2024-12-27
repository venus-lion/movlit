package movlit.be.book.application.converter;

import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;

public class BookcrewConverter {
    private BookcrewConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static BookcrewEntity toEntity(Bookcrew bookcrew) {
        return BookcrewEntity.builder()
                .crewId(bookcrew.getCrewId())
                .name(bookcrew.getName())
                .role(bookcrew.getRole())
                .profileImageUrl(bookcrew.getProfileImageUrl())
                .build();
    }

    // Entity -> Domain
    public static Bookcrew toDomain(BookcrewEntity bookcrewEntity) {
       return Bookcrew.builder()
               .crewId(bookcrewEntity.getCrewId())
               .name(bookcrewEntity.getName())
               .role(bookcrewEntity.getRole())
               .profileImageUrl(bookcrewEntity.getProfileImageUrl())
               .build();
    }
}
