package movlit.be.pub_sub.notification.infra.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.NotificationNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.domain.Notification;
import movlit.be.pub_sub.notification.infra.persistence.mongo.NotificationMongoRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository{

    private final NotificationMongoRepository notificationMongoRepository;

    @Override
    public List<Notification> findByMemberId(MemberId memberId) {
        // MongoDB memberId에 대한 알림 목록 조회
        List<Notification> notificationList = notificationMongoRepository.findByMemberId(memberId);

        if (notificationList == null || notificationList.isEmpty()) {
            throw new NotificationNotFoundException(); //
        }

        return notificationList;
    }

    @Override
    public void saveNotification(Notification notification) { notificationMongoRepository.save(notification); }

    @Override
    public void deleteById(String id) {
        notificationMongoRepository.deleteById(id);
    }

    @Override
    public void deleteAllByMemberId(MemberId memberId) {
        notificationMongoRepository.deleteAllByMemberId(memberId);
    }


}
