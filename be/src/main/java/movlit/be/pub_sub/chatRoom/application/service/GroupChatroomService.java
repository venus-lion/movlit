package movlit.be.pub_sub.chatRoom.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.pub_sub.chatRoom.domain.repository.GroupChatRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupChatroomService {

    private final GroupChatRepository groupChatRepository;

}
