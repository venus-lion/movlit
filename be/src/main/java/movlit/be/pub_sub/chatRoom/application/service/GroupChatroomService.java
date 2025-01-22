package movlit.be.pub_sub.chatRoom.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatroomService {

    private final GroupChatRepository groupChatRepository;
    private final MemberReadService memberReadService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHATROOM_MEMBERS_KEY_PREFIX = "chatroom:";
    private static final String CHATROOM_MEMBERS_KEY_SUFFIX = ":members";
    private static final long CHATROOM_MEMBERS_CACHE_TTL = 60 * 60; // 1시간

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

        if (existingGroupChatroom != null && member != null) {
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

        } else if (existingGroupChatroom == null && member != null) {
            throw new ChatroomNotFoundException();

        } else {
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

    public List<GroupChatroomMemberResponse> fetchMembersInGroupChatroom(GroupChatroomId groupChatroomId) {
        String cacheKey = CHATROOM_MEMBERS_KEY_PREFIX + groupChatroomId + CHATROOM_MEMBERS_KEY_SUFFIX;

        try {
            // Redis에서 캐시된 데이터 조회 (JSON 문자열)
            String cachedJson = (String) redisTemplate.opsForValue().get(cacheKey);
            List<GroupChatroomMemberResponse> response;

            if (cachedJson != null) {
                log.info("Cache hit for chatroom: {}", groupChatroomId);
                // JSON 문자열을 List<GroupChatroomMemberResponse>로 역직렬화
                response = objectMapper.readValue(cachedJson, new TypeReference<>() {});
                return response;
            }

            log.info("Cache miss for chatroom: {}", groupChatroomId);

            // 캐시에 데이터가 없으면 DB에서 조회
            // 채팅방 존재 여부 확인
            groupChatRepository.findByChatroomId(groupChatroomId);

            // 멤버 정보 조회
            response = groupChatRepository.findMembersByChatroomId(groupChatroomId);

            // 조회 결과를 JSON 문자열로 변환하여 Redis에 캐싱
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, json, CHATROOM_MEMBERS_CACHE_TTL, TimeUnit.SECONDS);

            return response;
        } catch (Exception e) {
            log.error("Error while fetching members from chatroom: {}", groupChatroomId, e);
            // 예외 처리 로직 추가 (예: 빈 리스트 반환 또는 예외 다시 던지기)
            return new ArrayList<>();
        }
    }
}