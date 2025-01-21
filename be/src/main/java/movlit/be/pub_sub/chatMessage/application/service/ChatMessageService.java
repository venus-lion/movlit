package movlit.be.pub_sub.chatMessage.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.RedisMessagePublisher;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;
import movlit.be.pub_sub.chatMessage.infra.persistence.mongo.ChatMessageMongoRepository;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageMongoRepository chatMessageMongoRepository;
    private final RedisMessagePublisher messagePublisher;

    public void sendMessageForOnOnOne(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessageType(MessageType.ONE_ON_ONE);
        saveMessageToMongoDB(chatMessageDto);
        messagePublisher.sendMessage(chatMessageDto);
    }

    public void sendMessageForGroup(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessageType(MessageType.GROUP);
        saveMessageToMongoDB(chatMessageDto);
        messagePublisher.sendMessage(chatMessageDto);
    }

    private void saveMessageToMongoDB(ChatMessageDto chatMessageDto) {
        // TODO : Converter 나중에 빼기
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(chatMessageDto.getRoomId())
                .senderId(chatMessageDto.getSenderId())
                .message(chatMessageDto.getMessage())
                .regDt(chatMessageDto.getRegDt())
                .messageType(chatMessageDto.getMessageType())
                .build();
        chatMessageMongoRepository.save(chatMessage);
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

}
