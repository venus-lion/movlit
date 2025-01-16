package movlit.be.pub_sub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 메시지 수신자(Subscriber) 구현
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    // WebSocket으로 메시지를 전달하기 위한 SimpMessagingTemplate
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message: {} from channel: {}", body, channel);
        // 여기서 수신된 메시지 처리 로직을 구현

        // 수신된 메시지를 모든 구독자에게 전송 (/topic/chat 으로).
        // ** 'ChatMessage' 같은 DTO로 파싱해서 보내는 것을 권장. **

        // /topic/chat/{roomId} 로 브로드캐스트
        // roomId를 어떻게 추출하느냐에 따라 다름(직렬화된 JSON에서 꺼낼 수도 있음)
        // 여기서는 예시로 roomId="test"라고 가정.
        String roomId = "test";
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, body);
    }

}
