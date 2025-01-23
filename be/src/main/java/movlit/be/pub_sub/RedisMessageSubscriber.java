package movlit.be.pub_sub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ContentTypeNotExistException;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto;
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

            // 1:1 채팅 메시지
            if (chatMessageDto.getMessageType() == MessageType.ONE_ON_ONE) {
                log.info("1:1 채팅 메시지");
                messagingTemplate.convertAndSend(
                        "/topic/chat/message/one-on-one/" + chatMessageDto.getRoomId(), chatMessageDto
                );
                return;
            }

            // 그룹 채팅 메시지
            if (chatMessageDto.getMessageType() == MessageType.GROUP) {
                log.info("일반 그룹 채팅 메시지");
                messagingTemplate.convertAndSend(
                        "/topic/chat/message/group/" + chatMessageDto.getRoomId(), chatMessageDto
                );
                return;
            }

            throw new ContentTypeNotExistException();

        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    public void updateRoom(String publishMessage) {
        try {
            UpdateRoomDto updateRoomDto = objectMapper.readValue(publishMessage, UpdateRoomDto.class);

            log.info("Received update room message: {}", publishMessage);
            messagingTemplate.convertAndSend(
                    "/topic/chat/message/group/" + updateRoomDto.getRoomId(), updateRoomDto
            );
            // 메시지를 필요에 따라 처리
        } catch (Exception e) {
            log.error("Exception in updateRoom {}", e);
        }
    }

    public void readMessage(String publishMessage) {
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            log.info("Received message to 'readMessage': {}", publishMessage);
            messagingTemplate.convertAndSend(
                    "/topic/chat/readMessage/", publishMessage
            );
            // 메시지를 필요에 따라 처리
        } catch (Exception e) {
            log.error("Exception in updateRoom {}");
        }
    }

}