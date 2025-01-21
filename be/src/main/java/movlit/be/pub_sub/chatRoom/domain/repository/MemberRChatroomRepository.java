package movlit.be.pub_sub.chatRoom.domain.repository;

import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;

public interface MemberRChatroomRepository {

    void save(MemberRChatroom memberRChatroom);



}
