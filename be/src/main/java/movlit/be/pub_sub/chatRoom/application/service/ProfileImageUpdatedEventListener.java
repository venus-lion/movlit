package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.RedisMessagePublisher;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import movlit.be.pub_sub.chatRoom.application.service.dto.ProfileImageUpdatedEvent;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto.EventType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProfileImageUpdatedEventListener {
    private final GroupChatroomService groupChatroomService;
    private final RedisMessagePublisher redisMessagePublisher;

    @TransactionalEventListener
    public void handleProfileImageUpdatedEvent(ProfileImageUpdatedEvent event){
        MemberId memberId = event.getMemberId();

        // 해당 멤버가 속한 모든 그룹채팅방 ID를 조회
        List<GroupChatroomResponseDto> groupChatroomResponseDtoList = groupChatroomService.fetchMyGroupChatList(memberId);

        // 각 그룹채팅방 ID에 대해 updatedRoomDto 생성 및 메세지 발행
        groupChatroomResponseDtoList.forEach(
            groupChatroomResponseDto -> {
                UpdateRoomDto updateRoomDto = new UpdateRoomDto(
                  groupChatroomResponseDto.getGroupChatroomId(),
                  MessageType.GROUP,
                  EventType.MEMBER_PROFILE_UPDATE,
                  memberId
                );

                redisMessagePublisher.updateRoom(updateRoomDto);
            }
        );
    }

}
