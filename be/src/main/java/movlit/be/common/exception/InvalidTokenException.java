package movlit.be.common.exception;

public class InvalidTokenException extends ResourceNotFoundException {

    public InvalidTokenException() {
        super(ErrorMessage.INVALID_TOKEN);
    }

}
