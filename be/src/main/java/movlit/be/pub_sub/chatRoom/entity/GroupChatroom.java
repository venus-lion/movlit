package movlit.be.pub_sub.chatRoom.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.ChatroomId;

@Entity
@Table(name = "group_chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupChatroom {

    @EmbeddedId
    private ChatroomId chatroomId;
    private String roomName;
    private String contentId; // MV_uuid, BK_uuid
    private LocalDateTime regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberRChatroom memberRChatroom;

    public GroupChatroom(ChatroomId chatroomId, String roomName, String contentId, LocalDateTime regDt,
                         MemberRChatroom memberRChatroom) {
        this.chatroomId = chatroomId;
        this.roomName = roomName;
        this.contentId = contentId;
        this.regDt = regDt;
        this.memberRChatroom = memberRChatroom;
    }

}
