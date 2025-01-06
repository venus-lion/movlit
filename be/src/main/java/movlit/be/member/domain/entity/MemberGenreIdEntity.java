package movlit.be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import movlit.be.common.util.ids.MemberId;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class MemberGenreIdEntity {

    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "id")
    private MemberId memberId;

}
