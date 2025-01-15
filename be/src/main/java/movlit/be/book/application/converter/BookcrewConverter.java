package movlit.be.book.application.converter;

import java.util.ArrayList;
import java.util.List;
import movlit.be.book.domain.BookcrewVo;
import movlit.be.book.domain.entity.BookcrewEntity;

public class BookcrewConverter {
    private BookcrewConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // DomainList -> EntityList
    public static List<BookcrewEntity> toEntityList(List<BookcrewVo> bookcrewVoList) {
        List<BookcrewEntity> bookcrewEntityList = new ArrayList<>();;

        for(BookcrewVo bookcrewVo : bookcrewVoList){
            BookcrewEntity bookcrewEntity = BookcrewEntity.builder()
                    .crewId(bookcrewVo.getCrewId())
                    .name(bookcrewVo.getName())
                    .role(bookcrewVo.getRole())
                    .profileImageUrl(bookcrewVo.getProfileImageUrl())
                    .build();

            bookcrewEntityList.add(bookcrewEntity);
        }
        return bookcrewEntityList;
    }

    // Domain -> Entity
    public static BookcrewEntity toEntity(BookcrewVo bookcrewVo) {

            return BookcrewEntity.builder()
                    .crewId(bookcrewVo.getCrewId())
                    .name(bookcrewVo.getName())
                    .role(bookcrewVo.getRole())
                    .profileImageUrl(bookcrewVo.getProfileImageUrl())
                    .build();
    }

    // EntityList -> DomainList
    public static List<BookcrewVo> toDomainList(List<BookcrewEntity> bookcrewEntityList) {
        List<BookcrewVo> bookcrewVoList = new ArrayList<>();;

        for(BookcrewEntity crewEntity : bookcrewEntityList){
            BookcrewVo bookcrewVo = BookcrewVo.builder()
                    .crewId(crewEntity.getCrewId())
                    .name(crewEntity.getName())
                    .role(crewEntity.getRole())
                    .profileImageUrl(crewEntity.getProfileImageUrl())
                    .build();

            bookcrewVoList.add(bookcrewVo);
        }

       return bookcrewVoList;
    }

    // Entity -> Domain
    public static BookcrewVo toDomain(BookcrewEntity bookcrewEntity) {

            return BookcrewVo.builder()
                    .crewId(bookcrewEntity.getCrewId())
                    .name(bookcrewEntity.getName())
                    .role(bookcrewEntity.getRole())
                    .profileImageUrl(bookcrewEntity.getProfileImageUrl())
                    .build();
    }
}
