package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatRoom;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.OneOnOneChatroomJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneOnOneChatroomService {

    private final OneOnOneChatroomJpaRepository oneOnOneChatRoomJpaRepository;

    public OneOnOneChatRoom createChatroom(String roomName,
                                           MemberId memberId) {
        OneOnOneChatRoom oneOnOneChatRoom = new OneOnOneChatRoom(memberId.getValue(), "memberB", roomName);
        return oneOnOneChatRoomJpaRepository.save(oneOnOneChatRoom);
    }

    public List<OneOnOneChatRoom> fetchAllChatrooms() {
        return oneOnOneChatRoomJpaRepository.findAll();
    }

    public OneOnOneChatRoom fetchChatroomById(Long roomId) {
        return oneOnOneChatRoomJpaRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
    }

}
