package movlit.be.pub_sub.chatRoom.application.convertor;

import java.time.LocalDateTime;
import movlit.be.common.util.IdFactory;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;

public class ChatroomConvertor {

    private ChatroomConvertor() {
    }

    public static GroupChatroom makeGroupChatroom(GroupChatroomRequest request, MemberEntity member) {
        MemberRChatroom memberRChatroom = makeMemberRChatroom(member);
        return makeGroupChatroom(request, memberRChatroom);
    }

    private static GroupChatroom makeGroupChatroom(GroupChatroomRequest request, MemberRChatroom memberRChatroom) {
        GroupChatroom groupChatroom = new GroupChatroom(IdFactory.createGroupChatroomId(), request.getRoomName(),
                request.getContentType(),
                LocalDateTime.now());
        groupChatroom.updateMemberRChatroom(memberRChatroom);
        return groupChatroom;
    }

    private static MemberRChatroom makeMemberRChatroom(MemberEntity member) {
        MemberRChatroom memberRChatroom = new MemberRChatroom(IdFactory.createMemberRChatroom(), LocalDateTime.now());
        memberRChatroom.addMember(member);
        return memberRChatroom;
    }

}
