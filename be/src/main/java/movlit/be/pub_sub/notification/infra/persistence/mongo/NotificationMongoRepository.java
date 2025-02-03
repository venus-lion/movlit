package movlit.be.pub_sub.notification.infra.persistence.mongo;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.domain.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface NotificationMongoRepository extends MongoRepository<Notification, String> {

    List<Notification> findByMemberId(MemberId memberId);

    void deleteAllByMemberId(MemberId memberId);

}
