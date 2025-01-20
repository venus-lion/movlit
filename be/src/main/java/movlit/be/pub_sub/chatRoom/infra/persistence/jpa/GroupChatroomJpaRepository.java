package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupChatroomJpaRepository extends JpaRepository<GroupChatroom, GroupChatroomId> {

    @Query("SELECT NEW movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto( "
            + "gc.groupChatroomId, "
            + "gc.contentId, "
            + "gc.roomName, "
            + "gc.regDt "
            + " ) FROM GroupChatroom gc "
            + "LEFT JOIN MemberRChatroom mrc on  mrc.memberRChatroomId = gc.memberRChatroom.memberRChatroomId "
            + "LEFT JOIN MemberEntity m on m.memberRChatroom.memberRChatroomId = mrc.memberRChatroomId "
            + "WHERE m.memberId = :memberId ")
    Optional<List<GroupChatroomResponseDto>> findAllByMemberId(@Param("memberId") MemberId memberId);

}


