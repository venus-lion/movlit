package movlit.be.pub_sub.chatRoom.domain.repository;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import org.springframework.data.repository.query.Param;

public interface GroupChatRepository {

    GroupChatroomResponse create(GroupChatroom groupChatroom);

    List<GroupChatroomMemberResponse> findMembersByChatroomId(@Param("chatroomId") GroupChatroomId chatroomId);
    GroupChatroom findByChatroomId(GroupChatroomId chatroomId);
}
