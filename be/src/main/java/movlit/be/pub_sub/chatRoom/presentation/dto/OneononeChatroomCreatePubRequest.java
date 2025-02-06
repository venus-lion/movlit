package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;

@Getter
@NoArgsConstructor
@ToString
public class OneononeChatroomCreatePubRequest {

    private OneononeChatroomId roomId;
    private MemberId topicReceiverId;

    public OneononeChatroomCreatePubRequest(OneononeChatroomId roomId, MemberId topicReceiverId) {
        this.roomId = roomId;
        this.topicReceiverId = topicReceiverId;
    }
}
