package movlit.be.pub_sub.chatRoom.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.application.service.OneOnOneChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomCreateResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomResponse;
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
public class OneOnOneChatroomController {

    private final OneOnOneChatroomService oneOnOneChatroomService;

    @GetMapping("/api/chat/oneOnOne")
    public ResponseEntity<List<OneOnOneChatroomResponse>> fetchMyOneOnOneChatList(
            @AuthenticationPrincipal MyMemberDetails details) {
        if (details == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MemberId memberId = details.getMemberId();
        List<OneOnOneChatroomResponse> response = oneOnOneChatroomService.fetchMyOneOnOneChatList(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/chat/create/oneOnOne")
    public ResponseEntity<OneOnOneChatroomCreateResponse> createOneOnOneChatroom(
            @RequestBody OneOnOneChatroomRequest request,
            @AuthenticationPrincipal MyMemberDetails details) {

        if (details == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        MemberId memberId = details.getMemberId();
        OneOnOneChatroomCreateResponse response = oneOnOneChatroomService.createOneononeChatroom(memberId, request);

        return ResponseEntity.ok(response);

    }

}
