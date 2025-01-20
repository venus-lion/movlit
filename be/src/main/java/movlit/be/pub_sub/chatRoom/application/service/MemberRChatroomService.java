package movlit.be.pub_sub.chatRoom.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.MemberRChatroomRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberRChatroomService {

    private final MemberRChatroomRepository memberRChatroomRepository;

    public void save(MemberRChatroom memberRChatroom) {
        memberRChatroomRepository.save(memberRChatroom);
    }

}
