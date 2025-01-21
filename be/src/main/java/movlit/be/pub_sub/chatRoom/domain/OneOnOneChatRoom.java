package movlit.be.pub_sub.chatRoom.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.OneOnOneChatroomId;

@Entity
@Table(name = "oneonone_chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneOnOneChatroom {

    @EmbeddedId
    private OneOnOneChatroomId oneOnOneChatroomId;

    private LocalDateTime regDt;

    @OneToMany(mappedBy = "oneOnOneChatroom", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberROneOnOneChatroom> memberROneOnOneChatrooms = new ArrayList<>();

    public OneOnOneChatroom(OneOnOneChatroomId oneOnOneChatroomId) {
        this.oneOnOneChatroomId = oneOnOneChatroomId;
        this.regDt = LocalDateTime.now();
    }

    public void updateMemberROneOnOneChatroom(MemberROneOnOneChatroom memberROneOnOneChatroom) {
        if (!this.memberROneOnOneChatrooms.contains(memberROneOnOneChatroom)) {
            this.memberROneOnOneChatrooms.add(memberROneOnOneChatroom);
        }

    }

}
