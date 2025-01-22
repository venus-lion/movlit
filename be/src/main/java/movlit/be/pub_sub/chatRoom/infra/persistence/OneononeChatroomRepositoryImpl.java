package movlit.be.pub_sub.chatRoom.infra.persistence;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.OneononeChatroomId;
import movlit.be.pub_sub.chatRoom.domain.MemberROneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.OneononeChatroom;
import movlit.be.pub_sub.chatRoom.domain.repository.OneononeChatroomRepository;
import movlit.be.pub_sub.chatRoom.infra.persistence.jpa.OneononeChatroomJpaRepository;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OneononeChatroomRepositoryImpl implements OneononeChatroomRepository {

    private final OneononeChatroomJpaRepository oneononeChatroomJpaRepository;

    @Override
    public OneononeChatroom create(OneononeChatroom oneononeChatroom) {
        return oneononeChatroomJpaRepository.save(oneononeChatroom);
    }

    @Override
    public List<OneononeChatroomResponse> fetchOneOnOneChatList(MemberId memberId) {
        List<OneononeChatroom> chatrooms = oneononeChatroomJpaRepository.findOneononeChatroomIdsByMemberId(
                memberId);
        if (chatrooms.isEmpty()) {
            return new ArrayList<>();
        }

        // 현재 로그인 유저 말고 상대방 정보 가져오기
        return chatrooms.stream()
                .flatMap(chatroom -> chatroom.getMemberROneononeChatrooms()
                        .stream()
                ) // 모든 MemberROneononeChatroom 스트림 생성
                .filter(mro -> !mro.getMember().getMemberId().equals(memberId))     // memberId가 일치하지 않는 항목 필터링
                .map(this::convertToResponse)
                .toList(); // 결과를 리스트로 수집
    }

    // 변환 메서드 분리
    private OneononeChatroomResponse convertToResponse(MemberROneononeChatroom memberROneononeChatroomRoom) {
        // 안전하게 값 추출
        OneononeChatroomId chatroomId = memberROneononeChatroomRoom.getOneononeChatroom().getOneononeChatroomId();
        MemberId memberId = memberROneononeChatroomRoom.getMember().getMemberId();
        String nickname = memberROneononeChatroomRoom.getMember().getNickname();
        String profileImgUrl = memberROneononeChatroomRoom.getMember().getProfileImgUrl();

        return new OneononeChatroomResponse(chatroomId, memberId, nickname, profileImgUrl);
    }

}
