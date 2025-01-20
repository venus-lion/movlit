package movlit.be.acceptance.group_chatroom;

import static movlit.be.acceptance.group_chatroom.GroupChatroomSteps.그룹_채팅_생성을_요청한다;
import static movlit.be.acceptance.group_chatroom.GroupChatroomSteps.그룹_채팅을_생성한다;
import static movlit.be.acceptance.group_chatroom.GroupChatroomSteps.상태코드가_200이다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("그룹 채팅 인수 테스트")
public class GroupChatroomTest extends AcceptanceTest {

    @DisplayName("그룹 채팅을 생성하는 데 성공하면, 상태코드 200과 body를 반환한다.")
    @Test
    void when_create_group_chat_then_response_200_and_body() {
        // docs
        api_문서_타이틀("createGroupChat_success", spec);

        // given
        String accessToken = 회원_원준_액세스토큰;

        // when
        var response = 그룹_채팅_생성을_요청한다(accessToken, spec);

        // then
        상태코드가_200이다(response);
    }

}
