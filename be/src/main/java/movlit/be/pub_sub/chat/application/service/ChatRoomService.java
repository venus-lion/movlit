package movlit.be.pub_sub.chat.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chat.infra.persistence.jpa.ChatRoomJpaRepository;
import movlit.be.pub_sub.entity.ChatRoom;
import movlit.be.pub_sub.entity.ContentType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    public ChatRoom createChatRoom(String roomName, ContentType roomContentType, MemberId memberId) {
        ChatRoom chatRoom = new ChatRoom(roomName, roomContentType, memberId);
        return chatRoomJpaRepository.save(chatRoom);
    }

    public List<ChatRoom> fetchAllChatRooms() {
        return chatRoomJpaRepository.findAll();
    }

    public ChatRoom fetchChatRoomById(Long roomId) {
        return chatRoomJpaRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
    }

}
