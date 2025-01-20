package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import movlit.be.common.util.ids.MemberRChatroomId;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRChatroomJpaRepository extends JpaRepository<MemberRChatroom, MemberRChatroomId> {

}
