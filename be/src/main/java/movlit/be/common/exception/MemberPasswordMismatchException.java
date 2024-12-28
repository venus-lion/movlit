package movlit.be.common.exception;

public class MemberPasswordMismatchException extends ResourceNotFoundException {

    public MemberPasswordMismatchException() {
        super(ErrorMessage.MEMBER_INCORRECT_PASSWORD);
    }

}
