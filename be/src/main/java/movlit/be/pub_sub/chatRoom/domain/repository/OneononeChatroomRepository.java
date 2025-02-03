package movlit.be.pub_sub.chatRoom.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;
import movlit.be.pub_sub.chatRoom.domain.MemberROneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;

import java.util.List;

public interface OneononeChatroomRepository {

    OneononeChatroom create(OneononeChatroom oneOnOneChatroom);

    List<OneononeChatroomResponse> fetchOneOnOneChatList(MemberId memberId);

    List<MemberROneononeChatroom> findWithMembersById(OneononeChatroomId roomId);

}
