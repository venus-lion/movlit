package movlit.be.pub_sub.chatRoom.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.FailedDeserializeException;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.domain.MemberROneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.OneononeChatroomRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneononeChatroomService {

    private final MemberReadService memberReadService;
    private final OneononeChatroomRepository oneOnOneChatroomRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public OneononeChatroomResponse createOneononeChatroom(MemberId memberId,
                                                           OneononeChatroomRequest request) {
        MemberEntity sender = memberReadService.findEntityByMemberId(memberId);
        MemberEntity receiver = memberReadService.findEntityByMemberId(request.getReceiverId());

        OneononeChatroom oneononeChatroom =
                new OneononeChatroom(IdFactory.createOneOnOneChatroomId());

        // sender의 일대일 채팅방 관계 entity set
        MemberROneononeChatroom senderROneononeChatroom =
                new MemberROneononeChatroom(IdFactory.createMemberROneOnOneChatroomId());
        senderROneononeChatroom.updateOneononeChatroom(oneononeChatroom);
        senderROneononeChatroom.updateMember(sender);
        oneononeChatroom.updateMemberROneononeChatroom(senderROneononeChatroom);

        // receiver의 일대일 채팅방 관계 entity set
        MemberROneononeChatroom receiverROneononeChatroom =
                new MemberROneononeChatroom(IdFactory.createMemberROneOnOneChatroomId());
        receiverROneononeChatroom.updateOneononeChatroom(oneononeChatroom);
        receiverROneononeChatroom.updateMember(receiver);
        oneononeChatroom.updateMemberROneononeChatroom(receiverROneononeChatroom);

        // DB 저장
        OneononeChatroom savedOneononeChatroom = oneOnOneChatroomRepository.create(oneononeChatroom);

        OneononeChatroomResponse response = new OneononeChatroomResponse(
                savedOneononeChatroom.getOneononeChatroomId(),
                receiver.getMemberId(),
                receiver.getNickname(),
                receiver.getProfileImgUrl()
        );

        // Redis에 채팅방 추가
        this.addOneononeChatroomToRedis(sender, response);
        this.addOneononeChatroomToRedis(receiver, response);

        return response;
    }

    public OneononeChatroomResponse fetchChatroomInfo(OneononeChatroomId roomId, MemberId currentMemberId) {
        MemberEntity otherMember = oneOnOneChatroomRepository.findWithMembersById(roomId)
                .stream()
                .filter(mro -> !mro.getMember().getMemberId().equals(currentMemberId))
                .findFirst()
                .orElseThrow(MemberNotFoundException::new)
                .getMember();
        return new OneononeChatroomResponse(
                roomId,
                otherMember.getMemberId(),
                otherMember.getNickname(),
                otherMember.getProfileImgUrl()
        );
    }

    public List<OneononeChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId) {

        String redisKey = "oneononeChatList:" + memberId.getValue();

        // Redis에서 채팅방 목록 조회
        List<String> cachedData = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (cachedData != null && !cachedData.isEmpty()) {
            List<OneononeChatroomResponse> list = cachedData.stream()
                    .map(this::deserializeChatroomResponse)
                    .toList();
            log.info("=== cash Hit : {}", list);
            return list;
        }

        // Redis에 데이터가 없으면 DB에서 조회 후 캐싱
        List<OneononeChatroomResponse> response = oneOnOneChatroomRepository.fetchOneOnOneChatList(memberId);

        // Redis에 채팅방 목록 캐시
        response.forEach(chatroom -> {
            String serializedChatroom = this.serializeChatroomResponse(chatroom);
            redisTemplate.opsForList().rightPush(redisKey, serializedChatroom);
        });

        return response;

    }

    private void addOneononeChatroomToRedis(MemberEntity memberEntity, OneononeChatroomResponse response) {
        String redisKey = "oneononeChatList:" + memberEntity.getMemberId().getValue();
        String serialzedChatroom = this.serializeChatroomResponse(response);

        // Redis 리스트에 채팅방 추가
        redisTemplate.opsForList().rightPush(redisKey, serialzedChatroom);
        // TTL 설정 (1시간)
        redisTemplate.expire(redisKey, Duration.ofHours(1));
    }

    // OneononeChatroomResponse 직렬화
    private String serializeChatroomResponse(OneononeChatroomResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize chatroom response", e);
        }
    }

    // OneononeChatroomResponse 역직렬화
    private OneononeChatroomResponse deserializeChatroomResponse(String data) {
        try {
            return objectMapper.readValue(data, OneononeChatroomResponse.class);
        } catch (JsonProcessingException e) {
            throw new FailedDeserializeException();
        }
    }

}