package movlit.be.pub_sub;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis Pub/Sub 테스트 컨트롤러
 */
@RestController
public class MessageController {

    private final RedisMessagePublisher publisher;

    public MessageController(RedisMessagePublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/publish")
    public String publish(@RequestParam String message) {
        publisher.publish(message);
        return "Message published!";
    }

}
