package movlit.be.pub_sub.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(10);
    private final Map<String, ScheduledFuture<?>> heartbeatTasks = new ConcurrentHashMap<>();
    private final Map<String, Boolean> emitterCompletionStatus = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String id) {
        SseEmitter emitter = createSseEmitter(id);

        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(System.currentTimeMillis()))
                    .name("connect")
                    .data("connected!")
                    .reconnectTime(45_000));
        } catch (IOException e) {
            log.error("Error sending connect event: {}", id, e);
            completeEmitter(id, e);
        }

        scheduleHeartbeat(id, emitter);
        emitters.put(id, emitter);
        emitterCompletionStatus.put(id, false);
        log.info("SSE emitter 추가 성공");
        return emitter;
    }

    public void sendNotificationToUser(String id, NotificationDto notification) {
        SseEmitter emitter = this.emitters.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
                log.info("==== sendNotificationToUser: {}", notification);
            } catch (IOException e) {
                handleEmitterError(id, e, "Error sending notification to user {}");
            }
        } else {
            log.info("SSE emitter not found: {}", id);
        }
    }

    private SseEmitter createSseEmitter(String id) {
        SseEmitter emitter = new SseEmitter(180_000L);
        emitter.onCompletion(() -> completeEmitter(id, null));
        emitter.onTimeout(() -> completeEmitter(id, new RuntimeException("Emitter timed out")));
        emitter.onError(e -> completeEmitter(id, e));
        return emitter;
    }

    private void scheduleHeartbeat(String id, SseEmitter emitter) {
        ScheduledFuture<?> heartbeat = SCHEDULER.scheduleAtFixedRate(() -> {
            SseEmitter currentEmitter = emitters.get(id);
            if (currentEmitter == null || emitterCompletionStatus.getOrDefault(id, false)) {
                log.debug("Emitter not found or completed, cancelling heartbeat: {}", id);
                cancelHeartbeat(id);
                return;
            }
            try {
                currentEmitter.send(SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .name("heartbeat")
                        .data("keep-alive"));
            } catch (IOException e) {
                log.warn("하트비트 전송 실패: {}", e.getMessage());
                handleEmitterError(id, e, "Heartbeat failed for user {}");
            }
        }, 0, 30, TimeUnit.SECONDS);

        heartbeatTasks.put(id, heartbeat);
    }

    private synchronized void cancelHeartbeat(String id) {
        ScheduledFuture<?> task = heartbeatTasks.remove(id);
        if (task != null) {
            task.cancel(true);
            log.debug("Cancelled heartbeat task for {}", id);
        }
    }

    private void handleEmitterError(String id, IOException e, String logMessage) {
        log.error(logMessage, id, e);
        completeEmitter(id, e);
    }

    private synchronized void completeEmitter(String id, Throwable throwable) {
        if (emitterCompletionStatus.getOrDefault(id, false)) {
            return; // 이미 완료된 경우 중복 처리 방지
        }

        SseEmitter emitter = emitters.get(id);
        if (emitter != null) {
            try {
                if (throwable != null) {
                    emitter.completeWithError(throwable);
                } else {
                    emitter.complete();
                }
            } finally {
                emitters.remove(id);
                emitterCompletionStatus.put(id, true);
                cancelHeartbeat(id);
            }
        }
    }

}
