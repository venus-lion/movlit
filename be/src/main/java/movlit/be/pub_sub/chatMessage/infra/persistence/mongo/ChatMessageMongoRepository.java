package movlit.be.pub_sub.chatMessage.infra.persistence.mongo;

import movlit.be.pub_sub.chatMessage.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, Long> {

}
