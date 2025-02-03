package movlit.be.pub_sub.notification;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.domain.Notification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final SseEmitterService sseEmitterService;
    private final NotificationService notificationService;

    @GetMapping(value = "/api/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable String id) {
        SseEmitter emitter = sseEmitterService.addEmitter(id);
        return ResponseEntity.ok().body(emitter);
    }

    // 알림 목록 가져오기
    @GetMapping("/api/notification")
    public ResponseEntity<List<Notification>> fetchNotification(@AuthenticationPrincipal MyMemberDetails details){
        if(details != null){
            MemberId memberId = details.getMemberId();
            List<Notification> notificationList = notificationService.fetchNotificationList(memberId);
            return ResponseEntity.ok().body(notificationList);

        }else
            return ResponseEntity.badRequest().build();

    }

    @DeleteMapping("/api/notification/{notiId}")
    public ResponseEntity deleteNotification(@PathVariable String notiId){
        notificationService.deleteNotificationById(notiId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/notification/all")
    public ResponseEntity deleteAllNotification(@AuthenticationPrincipal MyMemberDetails details){
        if(details != null){
            MemberId memberId = details.getMemberId();
            notificationService.deleteAllNotification(memberId);
            return ResponseEntity.ok().build();
        }else
            return ResponseEntity.badRequest().build();
    }


}
