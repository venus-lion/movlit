package movlit.be.pub_sub.notification;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.domain.Notification;
import movlit.be.pub_sub.notification.infra.persistence.NotificationRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

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
