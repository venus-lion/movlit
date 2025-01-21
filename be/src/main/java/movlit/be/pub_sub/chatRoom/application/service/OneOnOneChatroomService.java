package movlit.be.pub_sub.chatRoom.application.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.pub_sub.chatRoom.domain.MemberROneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.OneononeChatroomRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomCreateResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneononeChatroomService {

    private final MemberReadService memberReadService;
    private final OneononeChatroomRepository oneOnOneChatroomRepository;

    @Transactional
    public OneononeChatroomCreateResponse createOneononeChatroom(MemberId memberId,
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
        receiverROneononeChatroom.updateMember(receiver);
        oneononeChatroom.updateMemberROneononeChatroom(receiverROneononeChatroom);

        return oneOnOneChatroomRepository.create(oneononeChatroom);
    }

    public List<OneononeChatroomResponse> fetchMyOneOnOneChatList(MemberId memberId) {

        return oneOnOneChatroomRepository.fetchMyOneOnOneChatList(memberId);

    }

//    public OneOnOneChatroom fetchChatroomById(Long roomId) {
//        return oneOnOneChatRoomJpaRepository.findById(roomId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
//    }

}
