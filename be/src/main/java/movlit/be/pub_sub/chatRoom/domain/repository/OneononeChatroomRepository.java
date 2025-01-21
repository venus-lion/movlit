package movlit.be.pub_sub.chatRoom.domain.repository;

import java.util.List;

import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomCreateResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;

public interface OneononeChatroomRepository {

    OneononeChatroomCreateResponse create(OneononeChatroom oneOnOneChatroom);

    List<OneononeChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId);

}
