package movlit.be.pub_sub.chatMessage.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
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
    private MessageType messageType;

    @Builder
    public ChatMessage(Long roomId, MemberId senderId, String message, LocalDateTime regDt, MessageType messageType) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = regDt.toString();
        this.messageType = messageType;
    }

}