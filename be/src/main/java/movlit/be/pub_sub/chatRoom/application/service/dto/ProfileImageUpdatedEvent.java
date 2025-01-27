package movlit.be.pub_sub.chatRoom.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import movlit.be.common.util.ids.MemberId;

@Getter
@AllArgsConstructor
public class ProfileImageUpdatedEvent {
    private final MemberId memberId;
}
