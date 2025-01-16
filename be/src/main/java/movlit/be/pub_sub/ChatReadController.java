package movlit.be.pub_sub;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.pub_sub.message.presentation.dto.response.ChatMessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatReadController {

    private final ChatService chatService;

    /**
     * 채팅방에 접속할 때, 과거 메시지를 불러오기 위한 API
     * 예: GET /api/chat/history?roomId=123
     */
//    @GetMapping("/api/chat/history")
//    public List<ChatMessageDto> getChatHistory(String roomId) {
//        return chatService.getMessages(roomId);
//    }

}
