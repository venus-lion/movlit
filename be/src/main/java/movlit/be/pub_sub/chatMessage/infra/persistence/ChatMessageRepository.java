package movlit.be.pub_sub.chatMessage.infra.persistence;

import java.util.List;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;

public interface ChatMessageRepository {

    void saveMessage(ChatMessage chatMessage);

    List<ChatMessage> findByRoomId(String roomId);

}
