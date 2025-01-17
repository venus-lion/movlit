package movlit.be.pub_sub.chatMessage.presentation.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import movlit.be.common.util.ids.MemberId;
import org.springframework.data.annotation.CreatedDate;

@Getter
@ToString
@NoArgsConstructor
public class ChatMessageDto {

    private Long roomId;
    private MemberId senderId;
    private String message;
    @CreatedDate
    private LocalDateTime regDt;

    public ChatMessageDto(Long roomId, MemberId senderId, String message, LocalDateTime regDt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.regDt = regDt;
    }

    public ChatMessageDto(Long roomId, MemberId senderId, String message, String regDt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.regDt = LocalDateTime.parse(regDt);
    }

}
