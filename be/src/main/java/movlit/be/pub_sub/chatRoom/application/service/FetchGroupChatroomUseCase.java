package movlit.be.pub_sub.chatRoom.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.application.service.ChatMessageService;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FetchGroupChatroomUseCase {

    private final GroupChatroomService groupChatroomService;
    private final ChatMessageService chatMessageService;

    // 내가 가입한 그룹채팅 리스트 가져오기
    public List<GroupChatroomResponseDto> execute(MemberId memberId) {
        List<GroupChatroomResponseDto> chatroomList = groupChatroomService.fetchMyGroupChatroomList(memberId);

        return chatroomList.stream()
                .peek(chatroom -> {
                    ChatMessageDto recentMessage = chatMessageService.fetchRecentMessage(
                            chatroom.getGroupChatroomId().getValue());
                    if (Objects.nonNull(recentMessage)) {
                        chatroom.setRecentMessage(recentMessage);
                    }
                })
                .toList();
    }
}
