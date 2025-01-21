package movlit.be.pub_sub.chatRoom.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.OneononeChatroomRepository;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.OneononeChatroomJpaRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OneononeChatroomRepositoryImpl implements OneononeChatroomRepository {

    private final OneononeChatroomJpaRepository oneononeChatroomJpaRepository;

    @Override
    public OneononeChatroom create(OneononeChatroom oneononeChatroom) {
        return oneononeChatroomJpaRepository.save(oneononeChatroom);
    }

    @Override
    public List<OneononeChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId) {
        return oneononeChatroomJpaRepository.findOneononeChatroomsByMemberId(memberId);
    }

}
