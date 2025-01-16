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

@Entity
@Table(name = "oneonone_chat_room")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OneOnOneChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private String memberAId;
    private String memberBId;
    private LocalDateTime regDt;

    @Builder

    public OneOnOneChatRoom(String memberAId, String memberBId, String roomName) {
        this.memberAId = memberAId;
        this.memberBId = memberBId;
        this.roomName = roomName;
        this.regDt = LocalDateTime.now();
    }

}
