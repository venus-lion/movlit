package movlit.be.pub_sub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private ContentType roomContentType;
    private MemberId createdBy;
    private LocalDateTime regDt;

    @Builder
    public ChatRoom(String roomName, ContentType roomContentType, MemberId createdBy) {
        this.roomName = roomName;
        this.roomContentType = roomContentType;
        this.createdBy = createdBy;
        this.regDt = LocalDateTime.now();
    }

}
