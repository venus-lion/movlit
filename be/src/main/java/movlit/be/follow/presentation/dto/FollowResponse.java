package movlit.be.follow.presentation.dto;

import lombok.Builder;
import lombok.Data;
import movlit.be.common.util.ids.MemberId;

@Data
@Builder
public class FollowResponse {
    private MemberId memberId;
    private String nickname;
    private String profileImgUrl;
}
