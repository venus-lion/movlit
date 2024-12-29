package movlit.be.common.exception;

public class BookNotFoundException extends ResourceNotFoundException {

    public BookNotFoundException() {
        super(ErrorMessage.BOOK_NOT_FOUND);
    }

}
