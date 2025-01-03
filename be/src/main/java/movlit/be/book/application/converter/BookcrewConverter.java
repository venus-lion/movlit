package movlit.be.book.application.converter;

import java.util.ArrayList;
import java.util.List;
import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;

public class BookcrewConverter {
    private BookcrewConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // DomainList -> EntityList
    public static List<BookcrewEntity> toEntityList(List<Bookcrew> bookcrewList) {
        List<BookcrewEntity> bookcrewEntityList = new ArrayList<>();;

        for(Bookcrew bookcrew : bookcrewList){
            BookcrewEntity bookcrewEntity = BookcrewEntity.builder()
                    .crewId(bookcrew.getCrewId())
                    .name(bookcrew.getName())
                    .role(bookcrew.getRole())
                    .profileImageUrl(bookcrew.getProfileImageUrl())
                    .build();

            bookcrewEntityList.add(bookcrewEntity);
        }
        return bookcrewEntityList;
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

    // EntityList -> DomainList
    public static List<Bookcrew> toDomainList(List<BookcrewEntity> bookcrewEntityList) {
        List<Bookcrew> bookcrewList = new ArrayList<>();;

        for(BookcrewEntity crewEntity : bookcrewEntityList){
            Bookcrew bookcrew = Bookcrew.builder()
                    .crewId(crewEntity.getCrewId())
                    .name(crewEntity.getName())
                    .role(crewEntity.getRole())
                    .profileImageUrl(crewEntity.getProfileImageUrl())
                    .build();

            bookcrewList.add(bookcrew);
        }

       return bookcrewList;
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
