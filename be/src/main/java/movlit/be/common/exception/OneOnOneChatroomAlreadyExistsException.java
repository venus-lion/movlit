package movlit.be.common.exception;

public class OneOnOneChatroomAlreadyExistsException extends ResourceNotFoundException {

    public OneOnOneChatroomAlreadyExistsException() {
        super(ErrorMessage.ONEONONE_CHATROOM_ALREADY_EXISTS);
    }

}
