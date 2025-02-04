package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.common.util.ids.BookcrewId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class BookcrewVo {

    private BookcrewId crewId;
    private String name;
    private BookcrewEntity.Role role;
    private String profileImageUrl;

}
