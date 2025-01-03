package movlit.be.common.exception;

public class MovieCommentNotFound extends ResourceNotFoundException {

    public MovieCommentNotFound() {
        super(ErrorMessage.MOVIE_COMMENT_NOT_FOUND);
    }

}
