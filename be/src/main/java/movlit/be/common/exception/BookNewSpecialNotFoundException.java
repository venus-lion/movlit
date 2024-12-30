package movlit.be.common.exception;

public class BookNewSpecialNotFoundException extends ResourceNotFoundException {
    public BookNewSpecialNotFoundException() {
        super(ErrorMessage.BOOKNEWSPECIAL_NOT_FOUND);
    }

}
