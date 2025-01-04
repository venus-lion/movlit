package movlit.be.member.domain;

import lombok.Getter;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberEntity;

@Getter
public class MemberGenre {

    private MemberId memberId;
    private Long genreId;

//    private MemberGenreId memberGenreId;
//    private MemberEntity memberEntity;

}
