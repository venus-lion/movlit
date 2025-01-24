package movlit.be.pub_sub.notification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String id) {
        SseEmitter emitter = new SseEmitter(); // 타임아웃 설정 가능
        this.emitters.put(id, emitter);

        emitter.onCompletion(() -> this.emitters.remove(id));
        emitter.onTimeout(() -> this.emitters.remove(id));

        return emitter;
    }

    public void sendNotificationToUser(String id, NotificationDto notification) {
        SseEmitter emitter = this.emitters.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
            } catch (IOException e) {
                this.emitters.remove(id);
                log.error("Error sending notification to user {}", id, e);
            }
        }
    }

}
