package movlit.be.common.exception;

public class MovieHeartAlreadyExistsException extends ResourceNotFoundException {

    public MovieHeartAlreadyExistsException() {
        super(ErrorMessage.MOVIE_HEART_ALREADY_EXISTS);
    }

}
