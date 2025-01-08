package movlit.be.common.exception;

public class NotExistMovieHeartByMember extends ResourceNotFoundException {

    public NotExistMovieHeartByMember() {
        super(ErrorMessage.NOT_FOUND_MOVIE_HEART_BY_MEMBER);
    }

}
