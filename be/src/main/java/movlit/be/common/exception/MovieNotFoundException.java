package movlit.be.common.exception;

public class MovieNotFoundException extends ResourceNotFound {

    public MovieNotFoundException() {
        super(ErrorMessage.MOVIE_NOT_FOUND);
    }

}
