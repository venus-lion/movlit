package movlit.be.pub_sub.chatRoom.application.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupChatroomCreationWorker {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String GROUP_CHATROOM_QUEUE_KEY_PREFIX = "groupChatroomQueue:";

    @Async
    public Map<String, String> requestChatroomCreation(String contentId) {
        String queueKey = GROUP_CHATROOM_QUEUE_KEY_PREFIX + contentId;

        while (true) {
            // Redis Queue에서 memberId를 value로 꺼내옴 (RPOP)
            Object memberIdObject = redisTemplate.opsForList().rightPop(queueKey);

            if (memberIdObject == null) {
                // 큐가 비어있으면 종료
                break;
            }

            if (!(memberIdObject instanceof String memberId)) {
                log.error("Invalid memberId type for contentId: {}. Expected String, but got: {}", contentId,
                        memberIdObject.getClass().getName());
                continue;
            }

            // contentId와 memberId를 Map에 담아서 반환
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put(contentId, memberId);
            return resultMap;
        }
        return null;
    }

}