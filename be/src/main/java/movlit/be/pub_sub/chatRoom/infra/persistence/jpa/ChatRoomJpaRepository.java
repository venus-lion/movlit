package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import movlit.be.pub_sub.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

}
