package movlit.be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class MemberGenreIdEntity implements Serializable {

    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "id")
    private MemberId memberId;

}
