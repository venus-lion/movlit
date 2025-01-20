package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ChatroomNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.application.convertor.ChatroomConvertor;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.GroupChatRepository;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.GroupChatroomJpaRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatroomService {

    private final GroupChatroomJpaRepository groupChatroomJpaRepository;
    private final GroupChatRepository groupChatRepository;
    private final MemberReadService memberReadService;
    private final MemberRChatroomService memberRChatroomService;


    public List<GroupChatroomResponseDto> fetchMyGroupChatList(MemberId memberId) {
        if (memberId != null) {

            List<GroupChatroomResponseDto> myGroupChatList = groupChatroomJpaRepository.findAllByMemberId(memberId)
                    .orElseThrow(ChatroomNotFoundException::new);

            return myGroupChatList;
        } else {
            throw new ChatroomNotFoundException();
        }
    }

    @Transactional
    public GroupChatroomResponse createGroupChatroom(GroupChatroomRequest request, MemberId memberId) {
        MemberEntity member = memberReadService.findEntityByMemberId(memberId);
        MemberRChatroom memberRChatroom = ChatroomConvertor.makeMemberRChatroom(member);
        memberRChatroomService.save(memberRChatroom);
        GroupChatroom groupChatroom = ChatroomConvertor.makeGroupChatroom(request, memberRChatroom);
        return groupChatRepository.create(groupChatroom);

    }

}
