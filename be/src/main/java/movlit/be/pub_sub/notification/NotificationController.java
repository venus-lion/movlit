package movlit.be.pub_sub.notification;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/api/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable String id) {
        SseEmitter emitter = sseEmitterService.addEmitter(id);
        return ResponseEntity.ok().body(emitter);
    }


}
