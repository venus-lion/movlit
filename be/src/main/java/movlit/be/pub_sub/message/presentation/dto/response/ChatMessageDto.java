package movlit.be.pub_sub.message.presentation.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import movlit.be.common.util.ids.MemberId;

@Getter
@ToString
public class ChatMessageDto {

    private Long roomId;
    private MemberId senderId;
    private String message;
    private LocalDateTime regDt;

    public ChatMessageDto(Long roomId, MemberId senderId, String message) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.regDt = LocalDateTime.now();
    }

}
