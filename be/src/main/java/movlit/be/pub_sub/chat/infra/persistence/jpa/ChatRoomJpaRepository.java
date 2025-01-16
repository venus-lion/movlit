package movlit.be.pub_sub.chat.infra.persistence.jpa;

import movlit.be.pub_sub.entity.OneOnOneChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<OneOnOneChatRoom, Long> {

}
