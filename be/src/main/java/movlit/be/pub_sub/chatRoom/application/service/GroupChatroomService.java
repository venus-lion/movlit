package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ChatroomNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.GroupChatroomJpaRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatroomService {

    private final GroupChatroomJpaRepository groupChatroomJpaRepository;

    public List<GroupChatroomResponseDto> fetchMyGroupChatList(MemberId memberId){
        if(memberId != null){

            List<GroupChatroomResponseDto> myGroupChatList = groupChatroomJpaRepository.findAllByMemberId(memberId)
                    .orElseThrow(ChatroomNotFoundException::new);

            return myGroupChatList;
        }
        else {
            throw new ChatroomNotFoundException();
        }

    }

}
