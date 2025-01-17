package movlit.be.pub_sub.chatMessage.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_message")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    private ObjectId id;

    private Long roomId;
    private MemberId senderId;
    private String message;
    private String timestamp;               // TODO : 타입형태 협의 필요

    @Builder
    public ChatMessage(Long roomId, MemberId senderId, String message, LocalDateTime regDt) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = regDt.toString();
    }

}
