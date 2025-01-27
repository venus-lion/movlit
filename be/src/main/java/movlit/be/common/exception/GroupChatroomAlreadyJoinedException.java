package movlit.be.common.exception;

public class GroupChatroomAlreadyJoinedException extends ResourceNotFoundException {

    public GroupChatroomAlreadyJoinedException() {
        super(ErrorMessage.GROUPCHATROOM_ALREADY_JOINED);
    }

}
