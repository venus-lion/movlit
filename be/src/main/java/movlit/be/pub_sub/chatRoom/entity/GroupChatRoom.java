package movlit.be.pub_sub.chatRoom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Builder
    public GroupChatRoom(String roomName, ContentType roomContentType, String creatorId) {
        this.roomName = roomName;
        this.roomContentType = roomContentType;
        this.creatorId = creatorId;
        this.regDt = LocalDateTime.now();
    }

}
