package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;

/**
 * updateRoom Topic publish용 DTO
 * one-on-one / group chatroom 에서 활용 가능
 */
@Getter
public class UpdateRoomDto {

    //private String roomId;      // OneononeChatroomId, GroupChatroomId : getValue()로 넣어주기
    private GroupChatroomId groupChatroomId;
    private MessageType messageType;

    private EventType eventType; // ex) MEMBER_PROFILE_UPDATE
    private MemberId memberId; // "업데이트된" memberId

    public enum EventType {
        MEMBER_PROFILE_UPDATE; // 멤버 프로필 정보 업데이트
    }

    public UpdateRoomDto(GroupChatroomId groupChatroomId, MessageType messageType, EventType eventType, MemberId memberId) {
        this.groupChatroomId = groupChatroomId;
        this.messageType = messageType;
        this.eventType = eventType;
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "UpdateRoomDto{" +
                "groupChatroomId='" + groupChatroomId + '\'' +
                ", messageType=" + messageType +
                ", eventType=" + eventType +
                ", memberId=" + memberId.getValue();
    }

}
