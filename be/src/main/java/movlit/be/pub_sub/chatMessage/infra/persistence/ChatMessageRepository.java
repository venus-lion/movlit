package movlit.be.pub_sub.chatMessage.infra.persistence;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;

public interface ChatMessageRepository {

    void saveMessage(ChatMessage chatMessage);

    List<ChatMessage> findByRoomId(String roomId);

    Long findCountUnreadMessages(String roomId, MemberId memberId);

    List<ChatMessage> findUnreadMessages(String roomId, MemberId memberId);

}
