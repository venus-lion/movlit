package movlit.be.pub_sub.notification.infra.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.domain.Notification;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;


public interface NotificationRepository {
    List<Notification> findByMemberId(MemberId memberId);

    void saveNotification(Notification notification);

    void deleteById(String id);

    void deleteAllByMemberId(MemberId memberId);

}
