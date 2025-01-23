package movlit.be.ChatMessage.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;
import movlit.be.pub_sub.chatMessage.application.service.ChatMessageService;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;
import movlit.be.pub_sub.chatMessage.infra.persistence.mongo.ChatMessageMongoRepository;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatMessageServiceTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageMongoRepository chatMessageMongoRepository;

    private static final String ROOM_ID = "room123";
    private static final MemberId SENDER_ID = new MemberId("sender1");
    private static final MemberId RECEIVER_ID = new MemberId("receiver2");

    @BeforeEach
    void setup() {
        chatMessageMongoRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        chatMessageMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("특정 사용자의 읽지 않은 채팅방 개수를 올바르게 반환한다.")
    void givenOneononeChatroomId_whenMessageSend_thenGetUnreadMessageCount() {
        // given
        OneononeChatroomId oneononeChatroomId = new OneononeChatroomId(ROOM_ID);

        // when
        ChatMessage message1 = ChatMessage.builder()
                .roomId(ROOM_ID)
                .senderId(SENDER_ID)
                .message("안녕하세요.")
                .regDt(LocalDateTime.now())
                .messageType(MessageType.ONE_ON_ONE)
                .build();
        chatMessageMongoRepository.save(message1);

        ChatMessage message2 = ChatMessage.builder()
                .roomId(ROOM_ID)
                .senderId(SENDER_ID)
                .message("테스트 진행중입니다.")
                .regDt(LocalDateTime.now())
                .messageType(MessageType.ONE_ON_ONE)
                .build();
        chatMessageMongoRepository.save(message2);

        // then
        Long unreadMessageCount = chatMessageService.fetchCountUnreadMessages(
                oneononeChatroomId, RECEIVER_ID);

        assertEquals(2, unreadMessageCount);
    }

    @Test
    @DisplayName("특정 사용자의 읽지 않은 채팅방의 채팅들을 올바르게 반환한다.")
    void givenOneononeChatroomId_thenGetAllUnreadMessages() {
        // given
        OneononeChatroomId oneononeChatroomId = new OneononeChatroomId(ROOM_ID);

        // when
        ChatMessage message1 = ChatMessage.builder()
                .roomId(ROOM_ID)
                .senderId(SENDER_ID)
                .message("안녕하세요.")
                .regDt(LocalDateTime.now())
                .messageType(MessageType.ONE_ON_ONE)
                .build();
        chatMessageMongoRepository.save(message1);

        ChatMessage message2 = ChatMessage.builder()
                .roomId(ROOM_ID)
                .senderId(SENDER_ID)
                .message("테스트 진행중입니다.")
                .regDt(LocalDateTime.now())
                .messageType(MessageType.ONE_ON_ONE)
                .build();
        chatMessageMongoRepository.save(message2);

        // then
        List<ChatMessage> unreadMessages = chatMessageService.fetchUnreadMessages(
                oneononeChatroomId, RECEIVER_ID);

        assertEquals(2, unreadMessages.size());
        assertAll(
                () -> assertEquals(unreadMessages.get(0).getRoomId(), oneononeChatroomId.getValue()),
                () -> assertEquals(unreadMessages.get(0).getSenderId(), SENDER_ID),
                () -> assertEquals(unreadMessages.get(0).getMessage(), message1.getMessage())
        );
    }

}
