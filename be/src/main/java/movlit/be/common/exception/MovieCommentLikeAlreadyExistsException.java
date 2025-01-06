package movlit.be.common.exception;

public class MovieCommentLikeAlreadyExistsException extends ResourceNotFoundException {

    public MovieCommentLikeAlreadyExistsException() {
        super(ErrorMessage.MOVIE_COMMENT_LIKE_ALREADY_EXISTS);
    }

}
