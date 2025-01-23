package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import java.util.List;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OneononeChatroomJpaRepository extends JpaRepository<OneononeChatroom, OneononeChatroomId> {

    @Query("SELECT DISTINCT o FROM OneononeChatroom o " +
            "LEFT JOIN FETCH o.memberROneononeChatrooms " +
            "WHERE EXISTS (" +
            "  SELECT 1 FROM MemberROneononeChatroom mro " +
            "  WHERE mro.oneononeChatroom = o AND mro.member.memberId = :memberId" +
            ")")
    List<OneononeChatroom> findOneononeChatroomIdsByMemberId(MemberId memberId);

}
