package movlit.be.pub_sub.chatRoom.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberROneOnOneChatroomId;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.data.annotation.CreatedDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member_r_oneonone_chat_room")
public class MemberROneOnOneChatroom {

    @EmbeddedId
    private MemberROneOnOneChatroomId memberROneOnOneChatroomId;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    private OneOnOneChatroom oneOnOneChatroom;

    @CreatedDate
    private LocalDateTime regDt;

    public MemberROneOnOneChatroom(MemberROneOnOneChatroomId memberROneOnOneChatroomId) {
        this.memberROneOnOneChatroomId = memberROneOnOneChatroomId;
        this.regDt = LocalDateTime.now();
    }

}
