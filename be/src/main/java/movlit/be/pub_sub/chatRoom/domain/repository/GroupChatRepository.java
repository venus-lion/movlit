package movlit.be.pub_sub.chatRoom.domain.repository;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.data.repository.query.Param;

public interface GroupChatRepository {

    GroupChatroomResponse create(GroupChatroom groupChatroom);
    List<GroupChatroomResponseDto> findAllByMemberId(MemberId memberId);

}
