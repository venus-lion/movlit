package movlit.be.book.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.common.util.ids.BookcrewId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookCrewResponseDto {

    private BookcrewId crewId;
    private String name;
    private BookcrewEntity.Role role;
    private String profileImageUrl;

}
