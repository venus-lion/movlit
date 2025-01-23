package movlit.be.pub_sub.chatMessage.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;
import movlit.be.pub_sub.RedisMessagePublisher;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;
import movlit.be.pub_sub.chatMessage.infra.persistence.ChatMessageRepository;
import movlit.be.pub_sub.chatMessage.infra.persistence.mongo.ChatMessageMongoRepository;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RedisMessagePublisher messagePublisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String MESSAGE_QUEUE = "chat_message_queue";   // 큐 이름 (채팅방마다 별도의 큐를 사용할 수 있음)

    // 일대일 채팅방 sendMessage
    public void sendMessageForOnOnOne(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessageType(MessageType.ONE_ON_ONE);

        String chatMessageJson = convertToJson(chatMessageDto);
        redisTemplate.opsForList().rightPush(MESSAGE_QUEUE, chatMessageJson);

        // 비동기적으로 큐에서 메시지를 처리하는 작업을 시작
        processQueue();

        messagePublisher.sendMessage(chatMessageDto);
    }

    // 그룹 채팅방 sendMessage
    public void sendMessageForGroup(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessageType(MessageType.GROUP);

        String chatMessageJson = convertToJson(chatMessageDto);
        redisTemplate.opsForList().rightPush(MESSAGE_QUEUE, chatMessageJson);

        // 비동기적으로 큐에서 메시지를 처리하는 작업을 시작
        processQueue();

        messagePublisher.sendMessage(chatMessageDto);
    }

    // 해당 채팅방의 읽지 않은 메시지 갯수 return
    public Long fetchCountUnreadMessages(OneononeChatroomId roomId, MemberId memberId) {
        return chatMessageRepository.findCountUnreadMessages(roomId.getValue(), memberId);
    }

    // 해당 채팅방의 읽지 않은 메시지 return
    public List<ChatMessage> fetchUnreadMessages(OneononeChatroomId roomId, MemberId memberId) {
        return chatMessageRepository.findUnreadMessages(roomId.getValue(), memberId);
    }

    /**
     * ChatMessage MongoDB 저장
     */
    private void saveMessageToMongoDB(ChatMessageDto chatMessageDto) {
        // TODO : Converter 나중에 빼기
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatMessageDto.getRoomId())
                .senderId(chatMessageDto.getSenderId())
                .message(chatMessageDto.getMessage())
                .regDt(chatMessageDto.getRegDt())
                .messageType(chatMessageDto.getMessageType())
                .build();
        chatMessageRepository.saveMessage(chatMessage);
    }

    /**
     * 채팅방의 채팅목록 가져오기
     */
    public List<ChatMessageDto> fetchChatMessages(String roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(roomId);
        if (chatMessages.isEmpty()) {
            return new ArrayList<>();       // 빈 값 전달
        }

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
    // TODO : processQueue, convertToJson, convertFromJson 메서드는 ObjectMapper를 사용할 별개의 클래스를 둬서 관리하기
    public void processQueue() {
        // 비동기적으로 Redis 큐에서 메시지 처리
        // 큐에서 하나씩 메시지를 꺼내서 MongoDB에 저장
        new Thread(() -> {
            while (true) {
                String chatMessageJson = redisTemplate.opsForList().leftPop(MESSAGE_QUEUE);
                if (chatMessageJson != null) {
                    ChatMessageDto chatMessageDto = convertFromJson(chatMessageJson);
                    saveMessageToMongoDB(chatMessageDto);
                } else {
                    break;
                }
            }
        }).start();
    }

    // DTO를 JSON으로 변환하는 로직
    public String convertToJson(ChatMessageDto chatMessageDto) {
        try {
            return objectMapper.writeValueAsString(chatMessageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert message to JSON", e);
        }
    }

    // JSON을 DTO로 변환하는 로직
    public ChatMessageDto convertFromJson(String json) {
        try {
            return objectMapper.readValue(json, ChatMessageDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to message DTO", e);
        }
    }

}
