package movlit.be.pub_sub.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.notification.NotificationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private MemberId memberId;
    private String message;     // 팔로잉 : {원준}님이 {민지}님을 팔로우했습니다.
    // 알림 타입, 생성 시간 등 추가 필드 정의 가능
    private NotificationType type;  // 찜, 팔로잉, 일대일알림, 그룹채팅알림
    private String link = null; // 알림 클릭했을 때 넘어갈 url
    private String timestamp;

    public Notification(MemberId memberId, String message, NotificationType type, String timestamp) {

        this.memberId = memberId;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

}
