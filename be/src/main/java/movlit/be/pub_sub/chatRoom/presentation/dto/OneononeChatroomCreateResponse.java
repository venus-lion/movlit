package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import movlit.be.common.util.ids.OneononeChatroomId;

@Getter
public class OneononeChatroomCreateResponse {

    private OneononeChatroomId oneOnOneChatroomId;

    public OneononeChatroomCreateResponse(OneononeChatroomId oneOnOneChatroomId) {
        this.oneOnOneChatroomId = oneOnOneChatroomId;
    }

    public static OneononeChatroomCreateResponse of(OneononeChatroomId oneOnOneChatroomId) {
        return new OneononeChatroomCreateResponse(oneOnOneChatroomId);
    }

}
