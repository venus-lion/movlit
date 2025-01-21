package movlit.be.pub_sub.chatMessage.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.RedisMessagePublisher;
import movlit.be.pub_sub.chatMessage.entity.ChatMessage;
import movlit.be.pub_sub.chatMessage.infra.persistence.mongo.ChatMessageMongoRepository;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final RedisMessagePublisher messagePublisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String MESSAGE_QUEUE = "chat_message_queue";   // 큐 이름 (채팅방마다 별도의 큐를 사용할 수 있음)

    public ChatMessageDto processAndSendMessage(ChatMessageDto chatMessageDto) {

        String chatMessageJson = convertToJson(chatMessageDto);
        redisTemplate.opsForList().rightPush(MESSAGE_QUEUE, chatMessageJson);
        // 비동기적으로 큐에서 메시지를 처리하는 작업을 시작
        processQueue();

//        ChatMessage savedMessage = this.saveMessage(chatMessageDto);

        // 메시지 발송
        messagePublisher.sendMessage(chatMessageDto);

        return chatMessageDto;
    }

    /**
     * ChatMessage MongoDB 저장
     */
    private ChatMessage saveMessage(ChatMessageDto chatMessageDto) {
        // TODO : Converter 나중에 빼기
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatMessageDto.getRoomId())
                .senderId(chatMessageDto.getSenderId())
                .message(chatMessageDto.getMessage())
                .regDt(chatMessageDto.getRegDt())
                .build();
        return chatMessageMongoRepository.save(chatMessage);
    }

    /**
     * 채팅방의 채팅목록 가져오기
     */
    public List<ChatMessageDto> fetchChatMessages(Long roomId) {
        List<ChatMessage> chatMessages = chatMessageMongoRepository.findByRoomId(roomId);

        log.info("=== chatMessages : {}", chatMessages);
        // TODO : Converter 나중에 빼기
        return chatMessages.stream().map(
                c -> new ChatMessageDto(
                        c.getRoomId(),
                        c.getSenderId(),
                        c.getMessage(),
                        c.getTimestamp()
                )
        ).toList();
    }

    /**
     * Redis 큐 활용, 비동기 처리
     */
    private void processQueue() {
        // 비동기적으로 Redis 큐에서 메시지 처리
        // 큐에서 하나씩 메시지를 꺼내서 MongoDB에 저장
        new Thread(() -> {
            while (true) {
                String chatMessageJson = redisTemplate.opsForList().leftPop(MESSAGE_QUEUE);
                if (chatMessageJson != null) {
                    ChatMessageDto chatMessageDto = convertFromJson(chatMessageJson);
                    saveMessage(chatMessageDto);
                } else {
                    break;
                }
            }
        }).start();
    }

    // DTO를 JSON으로 변환하는 로직
    private String convertToJson(ChatMessageDto chatMessageDto) {
        try {
            return objectMapper.writeValueAsString(chatMessageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert message to JSON", e);
        }
    }

    // JSON을 DTO로 변환하는 로직
    private ChatMessageDto convertFromJson(String json) {
        try {
            return objectMapper.readValue(json, ChatMessageDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to message DTO", e);
        }
    }

}
