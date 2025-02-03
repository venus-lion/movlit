package movlit.be.common.exception;

public class FollowSelfNotAllowedException extends IllegalArgumentException {

    public FollowSelfNotAllowedException() {
        super(ErrorMessage.FOLLOW_SELF_NOT_ALLOWED);
    }

}
