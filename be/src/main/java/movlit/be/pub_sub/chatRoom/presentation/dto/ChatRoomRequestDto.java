package movlit.be.pub_sub.chatRoom.presentation.dto;

import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.entity.ContentType;

public record ChatRoomRequestDto(
        String roomName,
        MemberId createdBy,
        ContentType roomContentType
) {

}
