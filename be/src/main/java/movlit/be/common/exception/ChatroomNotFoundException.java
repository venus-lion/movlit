package movlit.be.common.exception;

public class ChatroomNotFoundException extends ResourceNotFoundException{
    public ChatroomNotFoundException(){
        super(ErrorMessage.CHATROOM_NOT_FOUND);
    }
}