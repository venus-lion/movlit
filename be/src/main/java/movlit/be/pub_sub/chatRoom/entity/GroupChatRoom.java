package movlit.be.pub_sub.chatRoom.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "group_chat_room")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    @Enumerated(EnumType.STRING)
    private ContentType roomContentType;
    private LocalDateTime regDt;
    private String creatorId;
    @Embedded
    private ParticipantIds participantIds;

    @Builder
    public GroupChatRoom(String roomName, ContentType roomContentType, String creatorId) {
        this.roomName = roomName;
        this.roomContentType = roomContentType;
        this.creatorId = creatorId;
        this.regDt = LocalDateTime.now();
    }

}
