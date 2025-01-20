package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupChatroomJpaRepository extends JpaRepository<GroupChatroom, GroupChatroomId> {

}
