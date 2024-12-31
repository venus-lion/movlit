package movlit.be.common.exception;

public class ExpiredTokenException extends ResourceNotFoundException {

    public ExpiredTokenException() {
        super(ErrorMessage.EXPIRED_TOKEN);
    }

}
