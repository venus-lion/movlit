package movlit.be.pub_sub.chatRoom.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.application.service.FetchMyOneononeChatroomUseCase;
import movlit.be.pub_sub.chatRoom.application.service.OneononeChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneononeChatroomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OneononeChatroomController {

    private final OneononeChatroomService oneononeChatroomService;
    private final FetchMyOneononeChatroomUseCase fetchMyOneononeChatroomUseCase;

    @GetMapping("/api/chat/oneOnOne")
    public ResponseEntity<List<OneononeChatroomResponse>> fetchMyOneOnOneChatList(
            @AuthenticationPrincipal MyMemberDetails details) {
        if (details == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MemberId memberId = details.getMemberId();
        List<OneononeChatroomResponse> response = fetchMyOneononeChatroomUseCase.execute(memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/chat/create/oneOnOne")
    public ResponseEntity<OneononeChatroomResponse> createOneOnOneChatroom(
            @RequestBody OneononeChatroomRequest request,
            @AuthenticationPrincipal MyMemberDetails details) {

        if (details == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MemberId memberId = details.getMemberId();
        OneononeChatroomResponse response = oneononeChatroomService.createOneononeChatroom(memberId, request);

        return ResponseEntity.ok(response);

    }

    // TODO : 채팅방 나가기

}
