package movlit.be.common.exception;

public class DuplicateEmailException extends ResourceNotFoundException {

    public DuplicateEmailException() {
        super(ErrorMessage.DUPLICATE_MEMBER_EMAIL);
    }

}
