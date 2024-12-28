package movlit.be.common.exception;

public class AuthHeaderNotFoundException extends ResourceNotFoundException {

    public AuthHeaderNotFoundException() {
        super(ErrorMessage.AUTH_HEADER_NOT_FOUND);
    }

}
