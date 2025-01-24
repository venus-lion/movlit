package movlit.be.pub_sub.chatRoom.domain.repository;

import java.util.List;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.data.repository.query.Param;

public interface GroupChatRepository {

    GroupChatroomResponse create(GroupChatroom groupChatroom);

    GroupChatroomResponseDto fetchRoomByContentId(String contentId);

    boolean existsByContentId(String contentId);

    List<GroupChatroomResponseDto> fetchGroupChatroomByMemberId(MemberId memberId);

    List<GroupChatroomMemberResponse> findMembersByChatroomId(@Param("chatroomId") GroupChatroomId chatroomId);

    GroupChatroom findByChatroomId(GroupChatroomId chatroomId);

}
