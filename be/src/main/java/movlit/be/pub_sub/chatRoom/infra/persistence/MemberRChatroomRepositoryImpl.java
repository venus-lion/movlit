package movlit.be.pub_sub.chatRoom.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.MemberRChatroomRepository;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.MemberRChatroomJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRChatroomRepositoryImpl implements MemberRChatroomRepository {

    private final MemberRChatroomJpaRepository memberRChatroomJpaRepository;

    @Override
    public void save(MemberRChatroom memberRChatroom) {
        memberRChatroomJpaRepository.save(memberRChatroom);
    }

}
