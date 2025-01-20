package movlit.be.pub_sub.chatRoom.presentation.dto;

import movlit.be.common.util.ids.MemberId;

public record ChatroomRequestDto(
        String roomName,
        MemberId createdBy
) {

}
