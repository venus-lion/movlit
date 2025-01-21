package movlit.be.acceptance.oneonone_chatroom;

import static movlit.be.acceptance.oneonone_chatroom.OneOnOneChatroomSteps.상태코드가_200이다;
import static movlit.be.acceptance.oneonone_chatroom.OneOnOneChatroomSteps.일대일_채팅을_생성한다;

import java.util.HashMap;
import java.util.Map;
import movlit.be.acceptance.AcceptanceTest;
import movlit.be.common.util.JwtTokenUtil;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.pub_sub.chatRoom.presentation.dto.OneOnOneChatroomRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("일대일 채팅 인수 테스트")
public class OneononeChatroomTest extends AcceptanceTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MemberReadService memberReadService;

    @DisplayName("일대일 채팅을 생성하는 데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_create_oneonone_chatroom_then_response_200_and_body() {
        // docs
        api_문서_타이틀("createOneononeChatroom_success", spec);

        // given
        String senderAccessToken = 회원_윤기_액세스토큰;

        String receiverAccessToken = 회원_원준_액세스토큰;
        MemberId receiverId = memberReadService.findByMemberEmail(jwtTokenUtil.extractEmail(receiverAccessToken))
                .getMemberId();
        Map<String, Object> request = new HashMap<>();
        request.put("receiverId", receiverId.getValue());
        // when
        var response = 일대일_채팅을_생성한다(senderAccessToken, request, spec);

        // then
        상태코드가_200이다(response);
    }

}
