package movlit.be.common.exception;

public class MovieHeartNotFoundException extends ResourceNotFoundException {

    public MovieHeartNotFoundException() {
        super(ErrorMessage.MOVIE_HEART_NOT_FOUND);
    }

}
