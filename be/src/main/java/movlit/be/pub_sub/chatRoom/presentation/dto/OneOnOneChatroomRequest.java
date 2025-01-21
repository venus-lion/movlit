package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;

@Getter
@NoArgsConstructor
public class OneOnOneChatroomRequest {

    private MemberId receiverId;

    public OneOnOneChatroomRequest(MemberId receiverId) {
        this.receiverId = receiverId;
    }

    public OneOnOneChatroomRequest(String receiverId) {
        this.receiverId = new MemberId(receiverId);
    }

}
