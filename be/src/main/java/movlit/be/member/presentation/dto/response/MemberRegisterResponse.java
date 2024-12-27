package movlit.be.member.presentation.dto.response;

import lombok.Getter;
import movlit.be.common.util.ids.MemberId;

@Getter
public class MemberRegisterResponse {

    private MemberId memberId;

    private MemberRegisterResponse(MemberId memberId) {
        this.memberId = memberId;
    }

    public static MemberRegisterResponse from(MemberId memberId) {
        return new MemberRegisterResponse(memberId);
    }

}
