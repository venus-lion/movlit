package movlit.be.common.exception;

public class TokenNotFoundException extends ResourceNotFoundException {

    public TokenNotFoundException() {
        super(ErrorMessage.INVALID_TOKEN);
    }

}
