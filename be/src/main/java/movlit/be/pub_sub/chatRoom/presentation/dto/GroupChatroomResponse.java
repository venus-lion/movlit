package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.GroupChatroomId;

@NoArgsConstructor
public class GroupChatroomResponse {

    private GroupChatroomId groupChatroomId;

    private GroupChatroomResponse(GroupChatroomId groupChatroomId) {
        this.groupChatroomId = groupChatroomId;
    }

    public GroupChatroomResponse of(GroupChatroomId groupChatroomId) {
        return new GroupChatroomResponse(groupChatroomId);
    }

}
