package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import movlit.be.pub_sub.chatRoom.entity.OneOnOneChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneOnOneChatRoomJpaRepository extends JpaRepository<OneOnOneChatRoom, Long> {

}
