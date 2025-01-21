package movlit.be.pub_sub.chatRoom.application.convertor;

import java.time.LocalDateTime;
import java.util.Objects;
import movlit.be.common.exception.ContentTypeNotExistException;
import movlit.be.common.util.IdFactory;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;

public class ChatroomConvertor {

    private ChatroomConvertor() {
    }

    public static MemberRChatroom makeNonReMemberRChatroom() {
        return new MemberRChatroom(IdFactory.createMemberRChatroom(), LocalDateTime.now());
    }

    public static GroupChatroom makeNonReGroupChatroom(GroupChatroomRequest request) {
        return new GroupChatroom(IdFactory.createGroupChatroomId(),
                request.getRoomName(),
                generateContentId(request.getContentType(), request.getContentId()),
                LocalDateTime.now());
    }

    private static String generateContentId(String contentType, Long contentId) {
        if (Objects.equals(contentType, "movie")) {
            return "MV_" + contentId;
        }

        if (Objects.equals(contentType, "book")) {
            return "BK_" + contentId;
        }

        throw new ContentTypeNotExistException();
    }

}
