package movlit.be.pub_sub.notification;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class SseEmitterService {

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(10);
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> heartbeatTasks = new ConcurrentHashMap<>();
    private final Map<String, Boolean> emitterCompletionStatus = new ConcurrentHashMap<>(); // emitter 완료 상태 추적

    public void sendNotificationToReceiver(String id, NotificationDto notification) {
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

    public SseEmitter addEmitter(String id) {
        SseEmitter emitter = getSseEmitter(id);

        try {
            // 초기 연결 시에 reconnectTime 설정
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(System.currentTimeMillis())) // 예시로 현재 시간을 사용, 고유한 ID 생성 필요
                    .name("connect")
                    .data("connected!")
                    .reconnectTime(45_000));
        } catch (IOException e) {
            log.error("Error sending connect event: {}", id, e);
            completeEmitter(id, e);
        }

        ScheduledFuture<?> heartbeat = getScheduledFuture(id, emitter);

        heartbeatTasks.put(id, heartbeat);
        emitters.put(id, emitter);
        emitterCompletionStatus.put(id, false); // 초기 상태는 false
        return emitter;
    }

    private ScheduledFuture<?> getScheduledFuture(String id, SseEmitter emitter) {
        ScheduledFuture<?> heartbeat = SCHEDULER.scheduleAtFixedRate(() -> {

            if (!emitters.containsKey(id) || emitterCompletionStatus.getOrDefault(id, false)) {
                log.debug("Emitter not found or already completed, cancelling heartbeat: {}", id);
                cancelHeartbeat(id);
                return;
            }
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis())) // 예시로 현재 시간을 사용, 고유한 ID 생성 필요
                        .name("heartbeat")
                        .data("keep-alive"));
            } catch (IOException e) {
                log.warn("하트비트 전송 실패: {}", e.getMessage());
                completeEmitter(id, e);
            }
        }, 0, 30, TimeUnit.SECONDS); // 30초 간격

        return heartbeat;
    }

    private SseEmitter getSseEmitter(String id) {
        SseEmitter emitter = new SseEmitter(180_000L); // 3분 타임아웃

        emitter.onCompletion(() -> {
            log.info("Emitter completed: {}", id);
            completeEmitter(id, null);
        });
        emitter.onTimeout(() -> {
            log.info("Emitter timed out: {}", id);
            completeEmitter(id, new RuntimeException("Emitter timed out")); // 또는 적절한 예외 객체 사용
        });
        emitter.onError(throwable -> {
            log.error("Emitter error: {}", id, throwable);
            completeEmitter(id, throwable);
        });

        return emitter;
    }

    // emitter 완료 시 처리
    private void completeEmitter(String id, Throwable throwable) {
        SseEmitter emitter = emitters.get(id);
        if (emitter != null) {
            if (throwable != null) {
                emitter.completeWithError(throwable);
            } else {
                emitter.complete();
            }
            emitters.remove(id);
            emitterCompletionStatus.put(id, true);
            cancelHeartbeat(id);
        }
    }

    private void cancelHeartbeat(String id) {
        ScheduledFuture<?> task = heartbeatTasks.get(id);
        if (task != null) {
            task.cancel(true);
            heartbeatTasks.remove(id);
        }
    }

}