package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatroom;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.OneOnOneChatroomJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneOnOneChatroomService {

    private final OneOnOneChatroomJpaRepository oneOnOneChatRoomJpaRepository;

    // TODO : 상대방 MemberId 받아서 entity setting
    public OneOnOneChatroom createChatroom(String roomName,
                                           MemberId memberId) {
        OneOnOneChatroom oneOnOneChatRoom = new OneOnOneChatroom(memberId.getValue(), "memberB");
        return oneOnOneChatRoomJpaRepository.save(oneOnOneChatRoom);
    }

    public List<OneOnOneChatroom> fetchAllChatrooms() {
        return oneOnOneChatRoomJpaRepository.findAll();
    }

    public OneOnOneChatroom fetchChatroomById(Long roomId) {
        return oneOnOneChatRoomJpaRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
    }

}
