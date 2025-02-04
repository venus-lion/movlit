package movlit.be.common.exception;

public class FollowNotFoundException extends ResourceNotFoundException {

    public FollowNotFoundException() {
        super(ErrorMessage.FOLLOW_NOT_FOUND);
    }

}
