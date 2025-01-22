package movlit.be.ChatMessage.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.application.service.ChatMessageService;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;
import movlit.be.pub_sub.chatMessage.infra.persistence.ChatMessageRepository;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class ChatMessageWriteServiceTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;
//    @InjectMocks
//    private ChatMessageMongoRepository chatMessageMongoRepository;

    @Autowired
    @Qualifier("threadPoolExecutor")
    private ThreadPoolExecutor taskExecutor;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(5);
//        taskExecutor.setMaxPoolSize(10);
//        taskExecutor.setQueueCapacity(25);
//        taskExecutor.initialize();
//
//        ReflectionTestUtils.setField(chatMessageService, "taskExecutor", taskExecutor);
//    }

    @Test
    void testProcessQueue_withThreadPoolTaskExecutor() throws InterruptedException {
        // Arrange
        ChatMessageDto chatMessageDto = new ChatMessageDto(
                "room1",
                new MemberId("tester1"),
                "Hello!",
                LocalDateTime.now(),
                MessageType.ONE_ON_ONE
        );
        String mockMessageJson = chatMessageService.convertToJson(chatMessageDto);

        when(redisTemplate.opsForList().leftPop("chat_message_queue"))
                .thenReturn(mockMessageJson, (String) null);

        CountDownLatch latch = new CountDownLatch(1000);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(chatMessageRepository).saveMessage(any(ChatMessage.class));

        // Act
        taskExecutor.execute(() -> {
            for (int i = 0; i < 1000; i++) {
                ReflectionTestUtils.invokeMethod(chatMessageService, "processQueue");
            }
        });

        // Assert
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        verify(redisTemplate, times(2000)).opsForList().leftPop("chat_message_queue");
        verify(chatMessageRepository, times(1000)).saveMessage(any(ChatMessage.class));
        assert completed : "Task did not complete within the timeout period";
    }

}
