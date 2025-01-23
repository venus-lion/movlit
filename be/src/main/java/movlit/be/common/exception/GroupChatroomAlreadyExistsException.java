package movlit.be.common.exception;

public class GroupChatroomAlreadyExistsException extends ResourceNotFoundException {

    public GroupChatroomAlreadyExistsException() {
        super(ErrorMessage.GROUPCHATROOM_ALREADY_EXISTS);
    }

}
