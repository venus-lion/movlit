package movlit.be.pub_sub.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private String userId;
    private String message;
    // 알림 타입, 생성 시간 등 추가 필드 정의 가능
}
