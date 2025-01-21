package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.GroupChatroomId;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ChatroomNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.application.convertor.ChatroomConvertor;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.MemberRChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.GroupChatRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatroomService {


    private final GroupChatRepository groupChatRepository;
    private final MemberReadService memberReadService;

//    public List<GroupChatroomResponseDto> fetchMyGroupChatList(MemberId memberId) {
//        if (memberId != null) {
//
//            List<GroupChatroomResponseDto> myGroupChatList = groupChatRepository.findAllByMemberId(memberId);
//            log.info("::GroupChatroomService_fetchMyGroupChatList::");
//            log.info(">> myGroupchatList : " + myGroupChatList.toString());
//
//            return myGroupChatList;
//        } else {
//            throw new ChatroomNotFoundException();
//        }
//    }

    @Transactional
    public GroupChatroomResponse createGroupChatroom(GroupChatroomRequest request, MemberId memberId) {
        GroupChatroom groupChatroom = ChatroomConvertor.makeNonReGroupChatroom(request);
        MemberRChatroom memberRChatroom = ChatroomConvertor.makeNonReMemberRChatroom();

        MemberEntity member = memberReadService.findEntityByMemberId(memberId);

        memberRChatroom.updateGroupChatRoom(groupChatroom);
        memberRChatroom.updateMember(member);
        groupChatroom.updateMemberRChatroom(memberRChatroom); // 그룹 채팅방에 멤버를 참여시킨다

        return groupChatRepository.create(groupChatroom);
    }

    public List<GroupChatroomMemberResponse> fetchMembersInGroupChatroom(GroupChatroomId groupChatroomId){
        // 채팅방 존재 여부 확인
        groupChatRepository.findByChatroomId(groupChatroomId);

        // 멤버 정보 조회
        List<GroupChatroomMemberResponse> members = groupChatRepository.findMembersByChatroomId(groupChatroomId);

        return members;
    }
}
