package movlit.be.common.exception;

public class MovieCrewNotFoundException extends ResourceNotFoundException {

    public MovieCrewNotFoundException() {
        super(ErrorMessage.MOVIE_NOT_FOUND);
    }

}
