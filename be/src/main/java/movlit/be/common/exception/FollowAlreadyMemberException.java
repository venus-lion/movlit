package movlit.be.common.exception;

public class FollowAlreadyMemberException extends IllegalArgumentException {

    public FollowAlreadyMemberException() {
        super(ErrorMessage.FOLLOW_ALREADY_MEMBER);
    }

}
