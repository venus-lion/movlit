package movlit.be.pub_sub;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.pub_sub.dto.response.ChatMessage;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 메시지를 Redis 리스트에 저장
     * 예: key = "chat:roomId"
     */
    public void saveMessage(ChatMessage chatMessage) {
        String key = "chat:" + chatMessage.getRoomId();
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPush(key, chatMessage);
    }

    /**
     * 방에 해당하는 메시지 목록 조회
     */
    @SuppressWarnings("unchecked")
    public List<ChatMessage> getMessages(String roomId) {
        String key = "chat:" + roomId;
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        Long size = listOps.size(key);
        if (size == null || size == 0) {
            return List.of();
        }
        // 0 ~ size-1 범위의 모든 요소 조회
        List<Object> rawList = listOps.range(key, 0, size - 1);
        if (rawList == null) {
            return List.of();
        }
        // 역직렬화된 Object를 ChatMessage로 캐스팅
        return rawList.stream()
                .map(obj -> (ChatMessage) obj)
                .toList();
    }

}
