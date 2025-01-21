package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import java.util.List;

import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneOnOneChatroomId;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OneononeChatroomJpaRepository extends JpaRepository<OneononeChatroom, OneOnOneChatroomId> {

    @Query("SELECT new movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse("
            + "o.oneOnOneChatroomId, mro.member.memberId, mro.member.nickname, mro.member.profileImgUrl)"
            + "FROM OneononeChatroom o "
            + "LEFT JOIN MemberROneononeChatroom mro "
            + "WHERE mro.member.memberId = :memberId")
    List<OneononeChatroomResponse> findOneononeChatroomsByMemberId(MemberId memberId);

}
