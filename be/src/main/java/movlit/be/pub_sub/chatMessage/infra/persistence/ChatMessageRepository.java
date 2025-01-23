package movlit.be.pub_sub.chatMessage.infra.persistence;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import java.util.Optional;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;

public interface ChatMessageRepository {

    void saveMessage(ChatMessage chatMessage);

    List<ChatMessage> findByRoomId(String roomId);


    Long findCountUnreadMessages(String roomId, MemberId memberId);

    List<ChatMessage> findUnreadMessages(String roomId, MemberId memberId);

    // 가장 최근 메시지 반환
    ChatMessage findTopByRoomIdOrderByTimestampDesc(String roomId);


}
