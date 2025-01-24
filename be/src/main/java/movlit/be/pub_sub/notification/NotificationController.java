package movlit.be.pub_sub.notification;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable MemberId memberId) {
        SseEmitter emitter = sseEmitterService.addEmitter(memberId);
        return ResponseEntity.ok(emitter);
    }

}
