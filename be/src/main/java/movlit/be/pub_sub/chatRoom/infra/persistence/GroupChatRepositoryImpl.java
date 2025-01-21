package movlit.be.pub_sub.chatRoom.infra.persistence;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.GroupChatroomNotFoundException;
import movlit.be.common.util.ids.GroupChatroomId;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.ChatroomNotFoundException;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.domain.GroupChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.GroupChatRepository;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.GroupChatroomJpaRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupChatRepositoryImpl implements GroupChatRepository {

    private final GroupChatroomJpaRepository groupChatroomJpaRepository;

    @Override
    public GroupChatroomResponse create(GroupChatroom groupChatroom) {
        GroupChatroom savedEntity = groupChatroomJpaRepository.save(groupChatroom);
        return GroupChatroomResponse.of(savedEntity.getGroupChatroomId());
    }

    @Override
    public List<GroupChatroomResponseDto> findAllByMemberId(MemberId memberId) {
        List<GroupChatroomResponseDto> myGroupChatList = groupChatroomJpaRepository.findAllByMemberId(memberId)
                .orElseThrow(ChatroomNotFoundException::new);

        return myGroupChatList;

    }

    @Override
    public GroupChatroom findByChatroomId(GroupChatroomId chatroomId) {
        return groupChatroomJpaRepository.findByGroupChatroomId(chatroomId)
                .orElseThrow(GroupChatroomNotFoundException::new);
    }

    @Override
    public List<GroupChatroomMemberResponse> findMembersByChatroomId(GroupChatroomId chatroomId) {
        List<GroupChatroomMemberResponse> members = groupChatroomJpaRepository.findMembersByChatroomId(
                chatroomId);

        // 특정 채팅방에 멤버가 존재하지 않는 경우 -> 빈 리스트 반환
        if (members.isEmpty() || members == null) {
            return List.of();
        }

        return members;
    }


}
