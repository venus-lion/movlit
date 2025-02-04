package movlit.be.common.exception;

import java.nio.file.AccessDeniedException;

public class ChatroomAccessDenied extends AccessDeniedException {

    public ChatroomAccessDenied() {
        super(String.valueOf(ErrorMessage.CHATROOM_ACCESS_DENIED));
    }

}
