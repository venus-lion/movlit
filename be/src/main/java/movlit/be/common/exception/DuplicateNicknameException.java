package movlit.be.common.exception;

public class DuplicateNicknameException extends ResourceNotFoundException {

    public DuplicateNicknameException() {
        super(ErrorMessage.DUPLICATE_MEMBER_NICKNAME);
    }

}
