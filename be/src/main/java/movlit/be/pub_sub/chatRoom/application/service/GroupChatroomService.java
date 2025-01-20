package movlit.be.pub_sub.chatRoom.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.application.convertor.ChatroomConvertor;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.GroupChatRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupChatroomService {

    private final GroupChatRepository groupChatRepository;
    private final MemberReadService memberReadService;

    public GroupChatroomResponse createGroupChatroom(GroupChatroomRequest request, MemberId memberId) {
        MemberEntity member = memberReadService.findEntityByMemberId(memberId);
        GroupChatroom groupChatroom = ChatroomConvertor.makeGroupChatroom(request, member);
        return groupChatRepository.create(groupChatroom);
    }

}
