package movlit.be.common.exception;

public class MovieNotFoundException extends ResourceNotFoundException {

    public MovieNotFoundException() {
        super(ErrorMessage.MOVIE_NOT_FOUND);
    }

}
