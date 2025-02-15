package movlit.be.pub_sub.chatMessage.infra.persistence.mongo;

import java.util.List;
import movlit.be.pub_sub.chatMessage.entity.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, ObjectId> {

    List<ChatMessage> findByRoomId(Long roomId);

}
