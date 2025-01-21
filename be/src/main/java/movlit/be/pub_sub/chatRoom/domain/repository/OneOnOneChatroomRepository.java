package movlit.be.pub_sub.chatRoom.domain.repository;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomCreateResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomResponse;

public interface OneOnOneChatroomRepository {

    OneOnOneChatroomCreateResponse create(OneOnOneChatroom oneOnOneChatroom);

    List<OneOnOneChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId);

}
