package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;

/**
 * updateRoom Topic publish용 DTO
 * one-on-one / group chatroom 에서 활용 가능
 */
@Getter
public class UpdateRoomDto {

    private String roomId;      // OneononeChatroomId, GroupChatroomId : getValue()로 넣어주기
    private MessageType messageType;

    // TODO : updateRoom 메서드 호출 시 필요한 정보들 정의

    public UpdateRoomDto(String roomId, MessageType messageType) {
        this.roomId = roomId;
        this.messageType = messageType;
    }

}
