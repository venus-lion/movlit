package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.application.service.ChatMessageService;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchMyOneononeChatroomUseCase {

    private final OneononeChatroomService oneononeChatroomService;
    private final ChatMessageService chatMessageService;

    // 내가 가입한 일대일채팅 리스트에 최근 메시지 더해주기
    public List<OneononeChatroomResponse> execute(MemberId memberId) {
        List<OneononeChatroomResponse> response = oneononeChatroomService.fetchMyOneOnOneChatList(memberId);

        return response.stream()
                .peek(chatroom -> {
                    ChatMessageDto recentMessage = chatMessageService.fetchRecentMessage(
                            chatroom.getRoomId().getValue());
                    if (Objects.nonNull(recentMessage)) {
                        chatroom.setRecentMessage(recentMessage);
                    }
                })
                .toList();
    }

}
