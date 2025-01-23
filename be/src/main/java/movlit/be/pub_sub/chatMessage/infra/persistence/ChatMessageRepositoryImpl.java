package movlit.be.pub_sub.chatMessage.infra.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;
import movlit.be.pub_sub.chatMessage.infra.persistence.mongo.ChatMessageMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessageMongoRepository chatMessageMongoRepository;

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageMongoRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> findByRoomId(String roomId) {
        return chatMessageMongoRepository.findByRoomId(roomId);
    }

    @Override
    public Long findCountUnreadMessages(String roomId, MemberId memberId) {
        return chatMessageMongoRepository.countUnreadMessages(roomId, memberId);
    }

    @Override
    public List<ChatMessage> findUnreadMessages(String roomId, MemberId memberId) {
        return chatMessageMongoRepository.findUnreadMessages(roomId, memberId);
    }

    public ChatMessage findTopByRoomIdOrderByTimestampDesc(String roomId) {
        ChatMessage latestMessage = chatMessageMongoRepository.findTopByRoomIdOrderByTimestampDesc(roomId).orElse(null);

        return latestMessage;
    }

}
