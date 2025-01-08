package movlit.be.common.exception;

public class MovieCommentLikeNotFoundException extends ResourceNotFoundException {

    public MovieCommentLikeNotFoundException() {
        super(ErrorMessage.MOVIE_COMMENT_LIKE_NOT_FOUND);
    }

}
