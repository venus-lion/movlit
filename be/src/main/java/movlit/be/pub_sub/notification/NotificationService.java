package movlit.be.pub_sub.notification;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.domain.Notification;
import movlit.be.pub_sub.notification.infra.persistence.NotificationRepository;
import org.bson.types.ObjectId;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.pub_sub.RedisNotificationPublisher;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatRoom.application.service.GroupChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberReadService memberReadService;
    private final RedisNotificationPublisher redisNotificationPublisher;
    private final GroupChatroomService groupChatroomService;  

    @Value("${share.url}")
    private String basicUrl;

    // 그룹 채팅방 메시지 전송 알림
    public void groupChatroomMessageNotification(ChatMessageDto chatMessageDto) {
        String roomName = groupChatroomService.fetchGroupChatroomById(new GroupChatroomId(chatMessageDto.getRoomId()))
                .getRoomName();
        GroupChatroomId groupChatroomId = new GroupChatroomId(chatMessageDto.getRoomId());
        List<GroupChatroomMemberResponse> responseList = groupChatroomService.fetchMembersInGroupChatroom(
                groupChatroomId);

        String senderNickname = memberReadService.findByMemberId(chatMessageDto.getSenderId()).getNickname();
        String message = NotificationMessage.generateGroupChatMessage(senderNickname, roomName,
                chatMessageDto.getMessage());

        // 발신자를 제외한 멤버들에게 알림 전송
        String senderId = chatMessageDto.getSenderId().getValue();
        String url = basicUrl + "/chatMain/" + chatMessageDto.getRoomId() + "/group";
        List<NotificationDto> notificationDtoList = responseList.stream()
                .filter(response -> !response.getMemberId().getValue().equals(senderId)) // 발신자 제외
                .map(response -> new NotificationDto(
                        response.getMemberId().getValue(),
                        message,
                        NotificationType.GROUP_CHAT,
                        url
                ))
                .toList();

        notificationDtoList.forEach(redisNotificationPublisher::publishNotification);

    }
  
  // 알림 목록 가져오기
    public List<Notification> fetchNotificationList(MemberId memberId){
        return notificationRepository.findByMemberId(memberId);
    }

    // 하나의 알림 삭제
    public void deleteNotificationById(String id){
        log.info("::NotificationService_deleteNotificationById::");
        log.info(">> deleted Noti Id " + id);
        notificationRepository.deleteById(id);
    }

    // 알림 전체 삭제
    public void deleteAllNotification(MemberId memberId){
        log.info("::NotificationService_deleteAllNotification::");
        log.info(">> deleted Noti memberId " + memberId.getValue());
        notificationRepository.deleteAllByMemberId(memberId);
    }

    // 알림 MongoDB 저장
    public void saveNotification( NotificationDto notificationdto){

        // Dto -> Document
        Notification notification = Notification.builder()
                        .memberId(new MemberId(notificationdto.getId()))
                        .message(notificationdto.getMessage())
                        .type(notificationdto.getType())
                        .timestamp(notificationdto.getTimestamp())
                        .build();


        notificationRepository.saveNotification(notification);
    }

}
