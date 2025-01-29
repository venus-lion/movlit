package movlit.be.pub_sub;



import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ContentTypeNotExistException;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.ChatMessageDto;
import movlit.be.pub_sub.chatMessage.presentation.dto.response.MessageType;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.UpdateRoomDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 메시지 수신자(Subscriber) 구현
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMessageSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberReadService memberReadService;

    private static final String CHATROOM_MEMBERS_KEY_PREFIX = "chatroom:";
    private static final String CHATROOM_MEMBERS_KEY_SUFFIX = ":members";
    private static final long CHATROOM_MEMBERS_CACHE_TTL = 60 * 60; // 1시간
    /**
     * Redis에서 메시지가 발행(publish)되면
     * 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);

            // 1:1 채팅 메시지
            if (chatMessageDto.getMessageType() == MessageType.ONE_ON_ONE) {
                log.info("1:1 채팅 메시지");
                messagingTemplate.convertAndSend(
                        "/topic/chat/message/one-on-one/" + chatMessageDto.getRoomId(), chatMessageDto
                );
                return;
            }

            // 그룹 채팅 메시지
            if (chatMessageDto.getMessageType() == MessageType.GROUP) {
                log.info("일반 그룹 채팅 메시지");
                messagingTemplate.convertAndSend(
                        "/topic/chat/message/group/" + chatMessageDto.getRoomId(), chatMessageDto
                );
                return;
            }

            throw new ContentTypeNotExistException();

        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    public void updateRoom(String publishMessage) {
        try {
//            // 1. publisher로부터 발행받은 updateRoomDto 체크
//            UpdateRoomDto updateRoomDto = objectMapper.readValue(publishMessage, UpdateRoomDto.class);
//            System.out.println("RedisMessageSubscriber >>> publisher부터 발행받은 updateRoomDto : " + updateRoomDto.toString());
//
//            GroupChatroomId groupChatroomId = updateRoomDto.getGroupChatroomId();
//            MemberId memberId = updateRoomDto.getMemberId(); // 프로필 업데이트된 특정 memberId
//
//            // 2. 캐시 키 생성
//            String cacheKey = CHATROOM_MEMBERS_KEY_PREFIX + groupChatroomId + CHATROOM_MEMBERS_KEY_SUFFIX;
//
//            // 3. Redis에서 캐시 업데이트
//            // Redis에서 현재 캐시된 데이터 조회
//            String cachedJson = (String) redisTemplate.opsForValue().get(cacheKey);
//
//            if (cachedJson != null){
//                // 캐시된 데이터(Json 문자열)를 List<GroupChatroomMemberResponse>로 역직렬화
//                List<GroupChatroomMemberResponse> cachedMembers = objectMapper.readValue(cachedJson, new TypeReference<>() {
//                });
//                for (GroupChatroomMemberResponse groupChatroomMemberResponse : cachedMembers){
//                    log.info("RedisMessageSubscriber >>> cached된 기존멤버 : {}", groupChatroomMemberResponse);
//                }
//
//                // 업데이트된 멤버 정보 조회
//                MemberEntity updatedMember = memberReadService.findEntityById(memberId);
//                log.info("RedisMessageSubscriber >>> 프로필업데이트된 멤버정보 : " + updatedMember.toStringExceptLazyLoading());
//
//                GroupChatroomMemberResponse updatedMemberResponse = new GroupChatroomMemberResponse(
//                        updatedMember.getMemberId(),
//                        updatedMember.getNickname(),
//                        updatedMember.getProfileImgUrl()
//                );
//
//                // 기존에 캐시된 멤버리스트에서, 업데이트된 멤버정보만 수정
//                for (int i = 0; i < cachedMembers.size(); i++){
//                    if (cachedMembers.get(i).getMemberId().equals(memberId)){
//                        cachedMembers.set(i, updatedMemberResponse);
//                        break;
//                    }
//                }
//
//                // 업데이트된 멤버 리스트를 다시 JSON 문자열로 변환하여 Redis에 캐싱
//                // 조회 결과를 JSON 문자열로 변환하여 Redis에 캐싱
//                String updatedJson = objectMapper.writeValueAsString(cachedMembers);
//                redisTemplate.opsForValue().set(cacheKey, updatedJson, CHATROOM_MEMBERS_CACHE_TTL, TimeUnit.SECONDS);

            // 1. publisher로부터 발행받은 updateRoomDto 체크
            UpdateRoomDto updateRoomDto = objectMapper.readValue(publishMessage, UpdateRoomDto.class);
            log.info("RedisMessageSubscriber >>> publisher부터 발행받은 updateRoomDto : " + updateRoomDto.toString());

            GroupChatroomId groupChatroomId = updateRoomDto.getGroupChatroomId();

            // 2. 캐시 키 생성 (roomId 사용)
            String cacheKey = CHATROOM_MEMBERS_KEY_PREFIX + groupChatroomId + CHATROOM_MEMBERS_KEY_SUFFIX;

            // 3. Redis에서 캐시된 데이터 조회
            String cachedJson = (String) redisTemplate.opsForValue().get(cacheKey);

            if (cachedJson != null){
                // 캐시된 데이터(Json 문자열)를 List<GroupChatroomMemberResponse>로 역직렬화
                List<GroupChatroomMemberResponse> cachedMembers = objectMapper.readValue(cachedJson, new TypeReference<>() {
                });

                // WebSocket 클라이언트한테 업데이트된 멤버정보 전송
                System.out.println("RedisMessageSubscriber >>>> roomId :: " + groupChatroomId);
                messagingTemplate.convertAndSend("/topic/chat/room/" + groupChatroomId.getValue(), cachedMembers);
            }

        } catch (Exception e) {
            log.error("Exception in updateRoom {}", e);
        }
    }

    public void readMessage(String publishMessage) {
        try {
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            log.info("Received message to 'readMessage': {}", publishMessage);
            messagingTemplate.convertAndSend(
                    "/topic/chat/readMessage/", publishMessage
            );
            // 메시지를 필요에 따라 처리
        } catch (Exception e) {
            log.error("Exception in updateRoom {}");
        }
    }

}