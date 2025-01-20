package movlit.be.pub_sub.chatRoom.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.GroupChatroomId;
import movlit.be.pub_sub.chatRoom.application.service.GroupChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomMemberResponse;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomRequest;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupChatroomController {

    private final GroupChatroomService groupChatroomService;

    @PostMapping("/api/chat/create/group")
    public ResponseEntity<GroupChatroomResponse> createGroupChatroom(@RequestBody @Valid GroupChatroomRequest request,
                                                                     @AuthenticationPrincipal MyMemberDetails myMemberDetails) {
        var response = groupChatroomService.createGroupChatroom(request, myMemberDetails.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 특정 그룹채팅방에 속한 모든 member 정보를 가져온다.
     *
     * @param chatroomId 채팅방 ID
     * @return ChatroomDto 채팅방 정보 및 멤버 리스트
     */
    @GetMapping("/api/chat/{chatroomId}/members")
    public ResponseEntity<List<GroupChatroomMemberResponse>> fetchMembersInGroupChatroom(@PathVariable GroupChatroomId chatroomId){
        var response = groupChatroomService.fetchMembersInGroupChatroom(
                chatroomId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
