package movlit.be.pub_sub.message.infra.persistence.mongo;

import movlit.be.pub_sub.message.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, Long> {

}
