package movlit.be.pub_sub.chatRoom.infra.persistence.jpa;

import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneOnOneChatroomJpaRepository extends JpaRepository<OneOnOneChatRoom, Long> {

}
