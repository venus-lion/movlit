package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;

@Getter
@NoArgsConstructor
@ToString
public class OneononeChatroomCreatePubDto {

    private OneononeChatroomId roomId;
    private MemberId topicReceiverId;
    private MemberId topicSenderId;
    private String topicSenderNickname;
    private String topicSenderProfileImgUrl;

    public OneononeChatroomCreatePubDto(OneononeChatroomId roomId, MemberId topicReceiverId, MemberId topicSenderId,
                                        String topicSenderNickname, String topicSenderProfileImgUrl) {
        this.roomId = roomId;
        this.topicReceiverId = topicReceiverId;
        this.topicSenderId = topicSenderId;
        this.topicSenderNickname = topicSenderNickname;
        this.topicSenderProfileImgUrl = topicSenderProfileImgUrl;
    }
}
