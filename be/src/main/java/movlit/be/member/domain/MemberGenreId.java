package movlit.be.member.domain;

import java.io.Serializable;
import lombok.Getter;
import movlit.be.common.util.ids.MemberId;

@Getter
public class MemberGenreId implements Serializable {

    private MemberId memberId;
    private Long genreId;

}
