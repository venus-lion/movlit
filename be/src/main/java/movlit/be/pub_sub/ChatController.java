package movlit.be.pub_sub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.dto.response.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RedisMessagePublisher redisPublisher;
    private final ObjectMapper objectMapper; // Bean 등록해두고 주입 받으면 좋음

    // 클라이언트가 "/app/chat/message" 로 STOMP 메시지를 보내면 이 메서드가 처리
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) throws Exception {
        log.info("Received chat message: {}", message);

        // 1) Redis에 메시지 저장
        chatService.saveMessage(message);

        // 메시지를 Redis Pub/Sub 채널에 발행
        // 여러 서버 인스턴스가 있다면, 모두가 이 메시지를 받아 WebSocket으로 전송
        //    - pub/sub 메시지는 JSON 형태로 직렬화해서 보낼 수 있음
        String jsonString = objectMapper.writeValueAsString(message);
        redisPublisher.publish(jsonString);
    }

}
