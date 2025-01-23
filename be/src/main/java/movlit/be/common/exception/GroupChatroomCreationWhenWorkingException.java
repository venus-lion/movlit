package movlit.be.common.exception;

public class GroupChatroomCreationWhenWorkingException extends ResourceNotFoundException {

    public GroupChatroomCreationWhenWorkingException() {
        super(ErrorMessage.GROUPCHATROOM_ASYNC_ERROR);
    }

}
