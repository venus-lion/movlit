package movlit.be.pub_sub.chatRoom.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.application.convertor.ChatroomConvertor;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.GroupChatRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupChatroomService {

    private final GroupChatRepository groupChatRepository;
    private final MemberReadService memberReadService;
    private final MemberRChatroomService memberRChatroomService;

    @Transactional
    public GroupChatroomResponse createGroupChatroom(GroupChatroomRequest request, MemberId memberId) {
        MemberEntity member = memberReadService.findEntityByMemberId(memberId);
        MemberRChatroom memberRChatroom = ChatroomConvertor.makeMemberRChatroom(member);
        memberRChatroomService.save(memberRChatroom);
        GroupChatroom groupChatroom = ChatroomConvertor.makeGroupChatroom(request, memberRChatroom);
        return groupChatRepository.create(groupChatroom);
    }

}
