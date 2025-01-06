package movlit.be.member.presentation.dto.response;

import lombok.Getter;
import movlit.be.common.util.ids.MemberId;

@Getter
public class MemberRegisterOAuth2Response {

    private MemberId memberId;

    private MemberRegisterOAuth2Response(MemberId memberId) {
        this.memberId = memberId;
    }

    public static MemberRegisterOAuth2Response from(MemberId memberId) {
        return new MemberRegisterOAuth2Response(memberId);
    }

}
