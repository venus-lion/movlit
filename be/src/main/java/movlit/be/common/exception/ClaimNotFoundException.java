package movlit.be.common.exception;

public class ClaimNotFoundException extends ResourceNotFoundException {

    public ClaimNotFoundException() {
        super(ErrorMessage.CLAIM_NOT_FOUND);
    }

}
