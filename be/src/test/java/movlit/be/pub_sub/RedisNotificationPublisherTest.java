package movlit.be.pub_sub;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import movlit.be.common.util.IdFactory;
import movlit.be.pub_sub.notification.NotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@ExtendWith(MockitoExtension.class)
class RedisNotificationPublisherTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ChannelTopic notificationTopic;

    @InjectMocks
    private RedisNotificationPublisher publisher;

    @Test
    void publishNotificationTest() {
        // Given
        NotificationDto notificationDto = new NotificationDto(IdFactory.createMemberId(), "Test Notification");
        when(notificationTopic.getTopic()).thenReturn("notification");

        // When
        publisher.publishNotification(notificationDto);

        // Then
        verify(redisTemplate, times(1)).convertAndSend("notification", notificationDto);
    }
}