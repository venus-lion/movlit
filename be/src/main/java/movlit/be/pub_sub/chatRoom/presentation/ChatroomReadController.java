package movlit.be.pub_sub.chatRoom.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.pub_sub.chatRoom.application.service.GroupChatroomService;
import movlit.be.pub_sub.chatRoom.application.service.OneOnOneChatroomService;
import movlit.be.pub_sub.chatRoom.presentation.dto.GroupChatroomResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
@Slf4j
public class ChatroomReadController {
    private final GroupChatroomService groupChatroomService;
    private final OneOnOneChatroomService oneOnOneChatroomService;



    @GetMapping("/myGroupChatrooms")
    public ResponseEntity fetchMyGroupChats(@AuthenticationPrincipal MyMemberDetails details){
        if(details != null){
            MemberId memberId = details.getMemberId();
            List<GroupChatroomResponseDto> myGroupChatListRes = groupChatroomService.fetchMyGroupChatList(memberId);

            return ResponseEntity.ok(myGroupChatListRes);

        }else{
            return ResponseEntity.badRequest().build();
        }

    }

}
