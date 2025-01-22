package movlit.be.member.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;

@Getter
@NoArgsConstructor
public class MemberIdResponse {

    private MemberId memberId;

    private MemberIdResponse(MemberId memberId) {
        this.memberId = memberId;
    }

    public static MemberIdResponse of(MemberId memberId) {
        return new MemberIdResponse(memberId);
    }

}
