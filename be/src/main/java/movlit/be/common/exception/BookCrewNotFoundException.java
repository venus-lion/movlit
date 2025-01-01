package movlit.be.common.exception;

public class BookCrewNotFoundException extends ResourceNotFoundException {

    public BookCrewNotFoundException() {
        super(ErrorMessage.BOOK_CREW_NOT_FOUND);
    }


}
