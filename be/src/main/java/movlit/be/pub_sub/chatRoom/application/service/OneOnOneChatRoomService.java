package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.entity.ContentType;
import org.springframework.stereotype.Service;
import movlit.be.pub_sub.chat.infra.persistence.jpa.OneOnOneChatRoomJpaRepository;
import movlit.be.pub_sub.entity.OneOnOneChatRoom;

@Service
@RequiredArgsConstructor
public class OneOnOneChatRoomService {

    private final OneOnOneChatRoomJpaRepository oneOnOneChatRoomJpaRepository;

    public movlit.be.pub_sub.entity.OneOnOneChatRoom createChatRoom(String roomName, ContentType roomContentType,
                                                                    MemberId memberId) {
        OneOnOneChatRoom oneOnOneChatRoom = new OneOnOneChatRoom(memberId.getValue(), "memberB", roomName);
        return oneOnOneChatRoomJpaRepository.save(oneOnOneChatRoom);
    }

    public List<OneOnOneChatRoom> fetchAllChatRooms() {
        return oneOnOneChatRoomJpaRepository.findAll();
    }

    public OneOnOneChatRoom fetchChatRoomById(Long roomId) {
        return oneOnOneChatRoomJpaRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
    }

}
