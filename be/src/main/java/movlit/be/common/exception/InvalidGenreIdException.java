package movlit.be.common.exception;

public class InvalidGenreIdException extends ResourceNotFoundException {

    public InvalidGenreIdException() {
        super(ErrorMessage.INVALID_GENRE_ID);
    }

}
