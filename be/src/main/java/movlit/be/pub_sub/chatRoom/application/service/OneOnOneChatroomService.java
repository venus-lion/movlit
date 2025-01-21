package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.domain.MemberROneOnOneChatroom;
import movlit.be.pub_sub.chatRoom.domain.OneOnOneChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.OneOnOneChatroomRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomCreateResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneOnOneChatroomService {

    private final MemberReadService memberReadService;
    private final OneOnOneChatroomRepository oneOnOneChatroomRepository;

    @Transactional
    public OneOnOneChatroomCreateResponse createOneononeChatroom(MemberId memberId,
                                                                 OneOnOneChatroomRequest request) {
        MemberEntity sender = memberReadService.findEntityByMemberId(memberId);
        MemberEntity receiver = memberReadService.findEntityByMemberId(request.getReceiverId());

        OneOnOneChatroom oneOnOneChatroom =
                new OneOnOneChatroom(IdFactory.createOneOnOneChatroomId());

        // sender의 일대일 채팅방 관계 entity set
        MemberROneOnOneChatroom senderROneOnOneChatroom =
                new MemberROneOnOneChatroom(IdFactory.createMemberROneOnOneChatroomId());
        senderROneOnOneChatroom.updateOneOnOneChatroom(oneOnOneChatroom);
        senderROneOnOneChatroom.updateMember(sender);
        oneOnOneChatroom.updateMemberROneOnOneChatroom(senderROneOnOneChatroom);

        // receiver의 일대일 채팅방 관계 entity set
        MemberROneOnOneChatroom receiverROneOnOneChatroom =
                new MemberROneOnOneChatroom(IdFactory.createMemberROneOnOneChatroomId());
        receiverROneOnOneChatroom.updateMember(receiver);
        oneOnOneChatroom.updateMemberROneOnOneChatroom(receiverROneOnOneChatroom);

        log.info("====oneOnOneChatroom: {}", oneOnOneChatroom);
        return oneOnOneChatroomRepository.create(oneOnOneChatroom);
    }

    public List<OneOnOneChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId) {

        return oneOnOneChatroomRepository.fetchMyOneOnOneChatList(memberId);

    }

//    public OneOnOneChatroom fetchChatroomById(Long roomId) {
//        return oneOnOneChatRoomJpaRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
//    }

}
