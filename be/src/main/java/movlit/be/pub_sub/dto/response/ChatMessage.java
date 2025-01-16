package movlit.be.pub_sub.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import movlit.be.common.util.ids.MemberId;

@Getter
public class ChatMessage {

    private String roomId;
    private MemberId senderId;
    private String content;
    private LocalDateTime regDt;

    public ChatMessage(String roomId, MemberId senderId, String content) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.regDt = LocalDateTime.now();
    }

}
