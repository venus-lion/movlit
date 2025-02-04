package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import movlit.be.common.util.ids.MemberROneOnOneChatroomId;
import movlit.be.pub_sub.chatRoom.domain.MemberROneononeChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberROneononeChatroomJpaRepository extends
        JpaRepository<MemberROneononeChatroom, MemberROneOnOneChatroomId> {

}
