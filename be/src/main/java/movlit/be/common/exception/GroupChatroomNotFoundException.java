package movlit.be.common.exception;

public class GroupChatroomNotFoundException extends ResourceNotFoundException{
    public GroupChatroomNotFoundException() {
        super(ErrorMessage.GROUPCHATROOM_NOT_FOUND);
    }

}
