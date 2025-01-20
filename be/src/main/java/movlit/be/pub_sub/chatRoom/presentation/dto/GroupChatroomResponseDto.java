package movlit.be.pub_sub.chatRoom.presentation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.GroupChatroomId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChatroomResponseDto {

    private GroupChatroomId groupChatroomId;
    private String contentId; // MV_movieId, BK_bookId
    private String roomName;
    private LocalDateTime regDt;


}
