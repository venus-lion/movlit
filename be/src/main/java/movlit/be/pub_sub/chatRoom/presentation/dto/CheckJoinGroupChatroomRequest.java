package movlit.be.pub_sub.chatRoom.presentation.dto;

import lombok.Getter;

public record CheckJoinGroupChatroomRequest(
        String contentId,
        String contentType
) {

}
