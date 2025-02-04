package movlit.be.common.exception;

public class BookIllegalArgumentException extends IllegalArgumentException {

    public BookIllegalArgumentException() {
        super(ErrorMessage.UNKNOWN_BOOK_ENTITY_TYPE);
    }

}
