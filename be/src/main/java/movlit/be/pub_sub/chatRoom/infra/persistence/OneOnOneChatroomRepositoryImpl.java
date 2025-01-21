package movlit.be.pub_sub.chatRoom.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.OneOnOneChatroomRepository;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.OneOnOneChatroomJpaRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomCreateResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OneOnOneChatroomRepositoryImpl implements OneOnOneChatroomRepository {

    private final OneOnOneChatroomJpaRepository oneOnOneChatroomJpaRepository;

    @Override
    public OneOnOneChatroomCreateResponse create(OneOnOneChatroom oneOnOneChatroom) {
        OneOnOneChatroom saved = oneOnOneChatroomJpaRepository.save(oneOnOneChatroom);

        return OneOnOneChatroomCreateResponse.of(saved.getOneOnOneChatroomId());
    }

    @Override
    public List<OneOnOneChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId) {
        return oneOnOneChatroomJpaRepository.findOneOnOneChatroomsByMemberId(memberId);
    }

}
