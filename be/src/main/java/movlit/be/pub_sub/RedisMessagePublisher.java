package movlit.be.pub_sub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * 메시지 발행자(Publisher) 구현
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisMessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic sendMessageTopic;
    private final ChannelTopic updateRoomTopic;

    /**
     * 채팅 보내기(sendMessage) 토픽 발행하는 메서드
     * @param chatMessageDto
     */
    public void sendMessage(ChatMessageDto chatMessageDto) {
//        // 1:1 메시지
//        if (chatMessageDto.getMessageType() == MessageType.ONE_ON_ONE) {
//            log.info("Publishing 1:1 chat message {}", chatMessageDto);
//            redisTemplate.convertAndSend(sendMessageTopic.getTopic(), chatMessageDto);
//            return;
//        }
//
//        // 그룹 메시지
//        if (chatMessageDto.getMessageType() == MessageType.GROUP) {
//            log.info("Publishing group chat message {}", chatMessageDto);
//            redisTemplate.convertAndSend(sendMessageTopic.getTopic(), chatMessageDto);
//            return;
//        }

        log.info("Publishing send message {}", chatMessageDto);
        redisTemplate.convertAndSend(sendMessageTopic.getTopic(), chatMessageDto);

//        throw new ContentTypeNotExistException();
    }

    public void updateRoom(UpdateRoomDto updateRoomDto) {
        log.info("Publishing update chatroom {}", updateRoomDto);
        redisTemplate.convertAndSend(updateRoomTopic.getTopic(), updateRoomDto);
    }

}