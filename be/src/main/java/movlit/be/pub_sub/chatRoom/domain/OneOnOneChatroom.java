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
import movlit.be.common.util.ids.OneononeChatroomId;

@Entity
@Table(name = "oneonone_chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneononeChatroom {

    @EmbeddedId
    private OneononeChatroomId oneononeChatroomId;

    private LocalDateTime regDt;

    @OneToMany(mappedBy = "oneononeChatroom", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberROneononeChatroom> memberROneononeChatrooms = new ArrayList<>();

    public OneononeChatroom(OneononeChatroomId oneononeChatroomId) {
        this.oneononeChatroomId = oneononeChatroomId;
        this.regDt = LocalDateTime.now();
    }

    public void updateMemberROneononeChatroom(MemberROneononeChatroom memberROneononeChatroom) {
        if (!this.memberROneononeChatrooms.contains(memberROneononeChatroom)) {
            this.memberROneononeChatrooms.add(memberROneononeChatroom);
        }

    }

}