package movlit.be.pub_sub.chatRoom.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.RedisMessagePublisher;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import movlit.be.pub_sub.chatRoom.application.service.dto.GroupChatroomJoinedEvent;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto.EventType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class GroupChatroomJoinedEventListener {
    private final GroupChatroomService groupChatroomService;
    private final RedisMessagePublisher redisMessagePublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MemberReadService memberReadService;

    private static final String CHATROOM_MEMBERS_KEY_PREFIX = "chatroom:";
    private static final String CHATROOM_MEMBERS_KEY_SUFFIX = ":members";
    private static final long CHATROOM_MEMBERS_CACHE_TTL = 60 * 60; // 1시간

    @TransactionalEventListener
    public void handleGroupChatroomJoinEvent(GroupChatroomJoinedEvent event) throws JsonProcessingException {
        GroupChatroomId groupChatroomId = event.getGroupChatroomId();
        MemberId newMemberId = event.getMemberId();

        // 1. 'ㅇㅇ(닉네임) 님이 가입하셨습니다.' 메세지 생성
        MemberEntity newMember = memberReadService.findEntityById(newMemberId);
        String joinMessage = newMember.getNickname() + " 님이 가입하셨습니다.";

        // 2. UpdateRoomDto 생성 및 발행
        UpdateRoomDto updateRoomDto = new UpdateRoomDto(
              groupChatroomId,
              MessageType.GROUP,
              EventType.MEMBER_JOIN,
              newMemberId,
              joinMessage // 입장메세지 설정
        );
        redisMessagePublisher.updateRoom(updateRoomDto);

        // 3. Redis 캐시 업데이트 (RedisMessageSubscriber 메세지 로직 유지)



    }

}
