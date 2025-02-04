package movlit.be.common.exception;

public class BookHeartNotFoundException extends ResourceNotFoundException {

    public BookHeartNotFoundException() {
        super(ErrorMessage.NOT_FOUND_BOOK_HEART_BY_MEMBER);
    }

}


