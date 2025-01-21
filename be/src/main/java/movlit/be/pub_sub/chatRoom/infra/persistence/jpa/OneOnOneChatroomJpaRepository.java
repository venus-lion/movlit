package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneOnOneChatroomId;
import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OneOnOneChatroomJpaRepository extends JpaRepository<OneOnOneChatroom, OneOnOneChatroomId> {

    @Query("SELECT new movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomResponse("
            + "o.oneOnOneChatroomId, mro.member.memberId, mro.member.nickname, mro.member.profileImgUrl)"
            + "FROM OneOnOneChatroom o "
            + "LEFT JOIN MemberROneOnOneChatroom mro "
            + "WHERE mro.member.memberId = :memberId")
    List<OneOnOneChatroomResponse> findOneOnOneChatroomsByMemberId(MemberId memberId);

}
