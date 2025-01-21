package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ChatroomAccessDenied;
import movlit.be.common.exception.ChatroomNotFoundException;
import movlit.be.common.util.ids.GroupChatroomId;
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
    private final MemberRChatroomService memberRChatroomService;


    // 그룹채팅 생성
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
    
    // 존재하는 그룹채팅방 가입
    public GroupChatroomResponse joinGroupChatroom(GroupChatroomId groupChatroomId, MemberId memberId)
            throws ChatroomAccessDenied {
        GroupChatroom existingGroupChatroom = groupChatRepository.findByChatroomId(groupChatroomId);
        MemberEntity member = memberReadService.findEntityByMemberId(memberId);

        log.info("::GroupChatroomService_joinGroupChatroom::");
        log.info(">> member : " + member.toString());
        log.info(">> groupChat to join : " + existingGroupChatroom.toString());

        if(existingGroupChatroom != null && member != null){
            // 관계테이블 row 생성 (row id 및 regDt생성)
            MemberRChatroom newMemberRChatroom = ChatroomConvertor.makeNonReMemberRChatroom();

            // 만든 관계 row에 member 정보 update
            newMemberRChatroom.updateMember(member);
            // 만든 관계 row에 chatroom 정보 update
            newMemberRChatroom.updateGroupChatRoom(existingGroupChatroom);
            log.info(">> newMemberRChatroom : " + newMemberRChatroom.toString());

            // 기존 채팅방에 새롭게 생성된 관계정보(memberRChatroom : 멤버-채팅방 관계) update
            existingGroupChatroom.updateMemberRChatroom(newMemberRChatroom);
            log.info(">> updated groupChat : " + existingGroupChatroom.toString());

        }else if(existingGroupChatroom == null && member !=null){
            throw new ChatroomNotFoundException();

        }else{
            throw new ChatroomAccessDenied();
        }

        // 바뀐 정보 업데이트
        return groupChatRepository.create(existingGroupChatroom);
    }


    // 내가 가입한 그룹채팅 리스트 가져오기
    public List<GroupChatroomResponseDto> fetchMyGroupChatList(MemberId memberId) {
        if (memberId != null) {

            List<GroupChatroomResponseDto> myGroupChatList = groupChatRepository.findAllByMemberId(memberId);
            log.info("::GroupChatroomService_fetchMyGroupChatList::");
            log.info(">> myGroupchatList : " + myGroupChatList.toString());

            return myGroupChatList;
        } else {
            throw new ChatroomNotFoundException();
        }
    }


    public List<GroupChatroomMemberResponse> fetchMembersInGroupChatroom(GroupChatroomId groupChatroomId){
        // 채팅방 존재 여부 확인
        groupChatRepository.findByChatroomId(groupChatroomId);

        // 멤버 정보 조회
        List<GroupChatroomMemberResponse> members = groupChatRepository.findMembersByChatroomId(groupChatroomId);

        return members;
    }

}
