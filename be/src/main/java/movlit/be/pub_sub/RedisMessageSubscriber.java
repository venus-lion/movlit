package movlit.be.pub_sub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * 메시지 수신자(Subscriber) 구현
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면
     * 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            log.info("Redis Subcriber - chatMSG : {}", chatMessageDto);

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            if (chatMessageDto.getMessageType() == MessageType.GROUP) {
                // 그룹 채팅 메시지인 경우
                log.info("그룹 채팅 메시지");
                messagingTemplate.convertAndSend(
                        // 여기에 message 부분부터 존재하지 않았음
                        "/topic/chat/message/group/" + chatMessageDto.getRoomId(), chatMessageDto
                );
                return;
            }

            // 1:1 채팅 메시지인 경우 (기존 로직 유지)
            log.info("1:1 채팅 메시지");
            messagingTemplate.convertAndSend(
                    "/topic/chat/message/one-on-one/" + chatMessageDto.getRoomId(), chatMessageDto
            );
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

}