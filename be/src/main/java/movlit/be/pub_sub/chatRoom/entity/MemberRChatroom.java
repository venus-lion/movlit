package movlit.be.pub_sub.chatRoom.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberRChatroomId;
import movlit.be.member.domain.entity.MemberEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member_r_chatroom")
public class MemberRChatroom {

    @EmbeddedId
    private MemberRChatroomId memberRChatroomId;

    @OneToMany(mappedBy = "memberRChatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberEntity> member;

    @OneToMany(mappedBy = "memberRChatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupChatroom> groupChatRoom;

    private LocalDateTime regDt;

}
