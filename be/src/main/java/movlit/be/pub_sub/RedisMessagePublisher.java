package movlit.be.pub_sub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ContentTypeNotExistException;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
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
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;

    public void sendMessage(ChatMessageDto chatMessageDto) {
        // 1:1 메시지
        if (chatMessageDto.getMessageType() == MessageType.ONE_ON_ONE) {
            log.info("Publishing 1:1 chat message {}", chatMessageDto);
            redisTemplate.convertAndSend(topic.getTopic(), chatMessageDto);
            return;
        }

        // 그룹 메시지
        if (chatMessageDto.getMessageType() == MessageType.GROUP) {
            log.info("Publishing group chat message {}", chatMessageDto);
            redisTemplate.convertAndSend(topic.getTopic(), chatMessageDto);
            return;
        }

        throw new ContentTypeNotExistException();
    }

}