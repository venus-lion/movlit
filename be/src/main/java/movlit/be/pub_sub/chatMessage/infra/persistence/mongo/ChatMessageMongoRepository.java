package movlit.be.pub_sub.chatMessage.infra.persistence.mongo;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.domain.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, ObjectId> {

    List<ChatMessage> findByRoomId(String roomId);

    /**
     * 특정 사용자의 읽지 않은 메시지 개수 조회
     */
    @Query(value = "{ 'roomId': ?0, 'readMembers': { $nin: [?1] } }", count = true)
    long countUnreadMessages(String roomId, MemberId memberId);

    /**
     * 특정 사용자의 읽지 않은 메시지 조회
     */
    @Query(value = "{ 'roomId': ?0, 'readMembers': { $nin: [?1] } }", count = true)
    List<ChatMessage> findUnreadMessages(String roomId, MemberId memberId);

}
