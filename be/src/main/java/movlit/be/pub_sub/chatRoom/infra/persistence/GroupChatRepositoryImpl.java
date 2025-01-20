package movlit.be.pub_sub.chatRoom.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.GroupChatroomJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupChatRepositoryImpl {

    private final GroupChatroomJpaRepository groupChatroomJpaRepository;



}
