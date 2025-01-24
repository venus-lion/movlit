package movlit.be.pub_sub.notification;

import lombok.Getter;

@Getter
public class NotificationMessage {

    private String senderNickname;
    private String recipientNickname;

    // 생성 예
    public static String generateFollowingMessage(String senderNickname, String recipientNickname) {
        return senderNickname + " 님이 " + recipientNickname + "님을 팔로우 하셨습니다.";
    }

}
