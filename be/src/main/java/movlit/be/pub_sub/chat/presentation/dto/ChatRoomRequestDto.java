package movlit.be.pub_sub.chat.presentation.dto;

import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.entity.ContentType;

public record ChatRoomRequestDto(
        String roomName,
        MemberId createdBy,
        ContentType roomContentType
) {

}
