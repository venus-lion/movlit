package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import movlit.be.common.util.ids.OneOnOneChatroomId;

@Getter
public class OneOnOneChatroomCreateResponse {

    private OneOnOneChatroomId oneOnOneChatroomId;

    public OneOnOneChatroomCreateResponse(OneOnOneChatroomId oneOnOneChatroomId) {
        this.oneOnOneChatroomId = oneOnOneChatroomId;
    }

    public static OneOnOneChatroomCreateResponse of(OneOnOneChatroomId oneOnOneChatroomId) {
        return new OneOnOneChatroomCreateResponse(oneOnOneChatroomId);
    }

}
