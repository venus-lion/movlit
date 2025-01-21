package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import movlit.be.common.util.ids.OneOnOneChatroomId;

@Getter
public class OneononeChatroomCreateResponse {

    private OneOnOneChatroomId oneOnOneChatroomId;

    public OneononeChatroomCreateResponse(OneOnOneChatroomId oneOnOneChatroomId) {
        this.oneOnOneChatroomId = oneOnOneChatroomId;
    }

    public static OneononeChatroomCreateResponse of(OneOnOneChatroomId oneOnOneChatroomId) {
        return new OneononeChatroomCreateResponse(oneOnOneChatroomId);
    }

}
