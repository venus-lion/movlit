package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import movlit.be.common.util.ids.MemberId;

@Getter
@AllArgsConstructor
public class GroupChatroomMemberResponse {
    private MemberId memberId;
    private String nickname;
    private String profileImgUrl;
}
