package movlit.be.pub_sub.chatRoom.domain.repository;

import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;

public interface GroupChatRepository {

    GroupChatroomResponse create(GroupChatroom groupChatroom);

}
