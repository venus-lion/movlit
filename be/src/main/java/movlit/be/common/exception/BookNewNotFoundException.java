package movlit.be.common.exception;

public class BookNewNotFoundException extends ResourceNotFoundException {

    public BookNewNotFoundException() {
        super(ErrorMessage.BOOKNEW_NOT_FOUND);
    }

}
