package movlit.be.pub_sub.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.pub_sub.RedisNotificationPublisher;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatRoom.application.service.GroupChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MemberReadService memberReadService;
    private final RedisNotificationPublisher redisNotificationPublisher;
    private final GroupChatroomService groupChatroomService;

    // 그룹 채팅방 메시지 전송 알림
    public void groupChatroomMessageNotification(ChatMessageDto chatMessageDto) {
//        String roomName = groupChatroomService.fetchGroupChatroomById(new GroupChatroomId(chatMessageDto.getRoomId())).getRoomName();
        List<NotificationDto> notificationDtoList = new ArrayList<>();
        GroupChatroomId groupChatroomId = new GroupChatroomId(chatMessageDto.getRoomId());
        List<GroupChatroomMemberResponse> responseList = groupChatroomService.fetchMembersInGroupChatroom(groupChatroomId);

        String senderNickname = memberReadService.findByMemberId(chatMessageDto.getSenderId()).getNickname();
        String message = NotificationMessage.generateGroupChatMessage(senderNickname, chatMessageDto.getRoomId(), chatMessageDto.getMessage());

        responseList.forEach(response -> {
            String id = response.getMemberId().getValue();
            NotificationDto notificationDto = new NotificationDto(
                    id, message, NotificationType.GROUP_CHAT
            );
            notificationDtoList.add(notificationDto);
        });

        notificationDtoList.forEach(notificationDto -> {
            redisNotificationPublisher.publishNotification(notificationDto);
        });
    }
}
