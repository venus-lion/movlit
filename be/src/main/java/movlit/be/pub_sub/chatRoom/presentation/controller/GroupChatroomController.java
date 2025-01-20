package movlit.be.pub_sub.chatRoom.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.pub_sub.chatRoom.application.service.GroupChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupChatroomController {

    private final GroupChatroomService groupChatroomService;

    @GetMapping("/api/chat/create/group")
    public ResponseEntity<GroupChatroomResponse> createGroupChatroom(@RequestBody @Valid GroupChatroomRequest request,
                                                                     @AuthenticationPrincipal MyMemberDetails myMemberDetails) {
        var response = groupChatroomService.createGroupChatroom(request, myMemberDetails.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
