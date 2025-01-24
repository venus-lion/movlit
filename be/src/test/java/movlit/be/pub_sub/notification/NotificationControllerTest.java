package movlit.be.pub_sub.notification;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import movlit.be.pub_sub.RedisNotificationPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisNotificationPublisher publisher;

    @Test
    // 인수 테스트에서 accessToken 받아오는 로직 -> Mock 테스트에서는 이렇게
    // Spring Security에서 test를 할 때 Mock으로 주입해주는 서포터 (권한 용도)
    @WithMockUser
    void subscribeAndReceiveNotificationTest() throws Exception {
        // Given
        String userId = "0s0d0d029309df";
        NotificationDto notificationDto = new NotificationDto(userId, "Test Notification");

        // When
        MvcResult result = mockMvc.perform(get("/subscribe/" + userId)
                        .accept(MediaType.TEXT_EVENT_STREAM))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        // 1. SSE 연결 확인 (이 부분은 별도의 라이브러리나 수동 검증 필요)
        // ...

        // 2. 알림 발행
        publisher.publishNotification(notificationDto);

        // 3. SSE를 통해 수신된 메시지 검증 (이 부분은 비동기 처리 필요)
        // 예: Awaitility 라이브러리 사용
        await().atMost(5, SECONDS).until(() -> {
            String content = response.getContentAsString(); // 누적된 메시지 확인
            return content.contains("Test Notification");
        });
    }

}