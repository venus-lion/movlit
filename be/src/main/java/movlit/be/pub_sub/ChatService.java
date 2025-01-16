package movlit.be.pub_sub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

//    private final RedisMessagePublisher redisPublisher;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    /**
//     * 채팅방에 메시지 발송
//     */
//    public void sendChatMessage(ChatMessageDto chatMessageDto) {
//        // 0. redis에 해당 채팅방roomId(key)에 마지막 메세지(value)를 넣어준다.
////        chatRoomRedisRepository.setLastChatMessage(chatMessage.getRoomId(), chatMessage);
//        this.saveMessage(chatMessageDto);
//
//        MemberId senderId = chatMessageDto.getSenderId();
//        MemberId recipientId = chatMessageDto.getRecipientId();
//
//        // 1. 채팅방이 삭제되는 것이라면 delete 를 해준다.
//        if (chatMessageDto.getType().equals(MessageType.DELETE)) {
//            chatRoomService.deleteChatRoom(accessToken, chatMessageDto.getRoomId(), userId);
//            chatRoomRedisRepository.deleteChatRoom(userId, chatMessageDto.getRoomId());
//        }
//
//        // 2. 채팅방 리스트에 새로운 채팅방 정보가 없다면, 넣어준다. 마지막 메시지도 같이 담는다. 상대방 레디스에도 업데이트 해준다.
//        setNewChatRoomInfo(chatMessageDto, newChatRoomList);
//
//        // 3. 마지막 메시지들이 담긴 채팅방 리스트들을 가져온다.
//        List<ChatRoomListGetResponse> chatRoomListGetResponseList = chatRoomService.getChatRoomList(userId,
//                accessToken);
//        // 4. 파트너 채팅방 리스트도 가져온다. (파트너는 userId 로만)
//        List<ChatRoomListGetResponse> partnerChatRoomGetResponseList = getChatRoomListByUserId(partnerId);
//
//        // 5. 마지막 메세지 기준으로 정렬 채팅방 리스트 정렬
//        chatRoomListGetResponseList = chatRoomService.sortChatRoomListLatest(chatRoomListGetResponseList);
//        partnerChatRoomGetResponseList = chatRoomService.sortChatRoomListLatest(partnerChatRoomGetResponseList);
//
//        MessageSubDto messageSubDto = MessageSubDto.builder()
//                .userId(userId)
//                .partnerId(partnerId)
//                .chatMessageDto(chatMessageDto)
//                .list(chatRoomListGetResponseList)
//                .partnerList(partnerChatRoomGetResponseList)
//                .build();
//
//        redisPublisher.publish(messageSubDto);
//    }
//
//    /**
//     * 메시지를 Redis 리스트에 저장
//     * 예: key = "chat:roomId"
//     */
//    private void saveMessage(ChatMessageDto chatMessageDto) {
//        String key = "chat:" + chatMessageDto.getRoomId();
//        ListOperations<String, Object> listOps = redisTemplate.opsForList();
//        listOps.rightPush(key, chatMessageDto);
//    }
//
//    /**
//     * 방에 해당하는 메시지 목록 조회
//     */
//    private List<ChatMessageDto> getMessages(String roomId) {
//        String key = "chat:" + roomId;
//        ListOperations<String, Object> listOps = redisTemplate.opsForList();
//        Long size = listOps.size(key);
//        if (size == null || size == 0) {
//            return List.of();
//        }
//        // 0 ~ size-1 범위의 모든 요소 조회
//        List<Object> rawList = listOps.range(key, 0, size - 1);
//        if (rawList == null) {
//            return List.of();
//        }
//        // 역직렬화된 Object를 ChatMessage로 캐스팅
//        return rawList.stream()
//                .map(obj -> (ChatMessageDto) obj)
//                .toList();
//    }

}
