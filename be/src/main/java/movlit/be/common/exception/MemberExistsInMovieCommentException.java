package movlit.be.common.exception;

public class MemberExistsInMovieCommentException extends ResourceNotFoundException {

    public MemberExistsInMovieCommentException() {
        super(ErrorMessage.MEMBER_EXISTS_IN_MOVIE_COMMENT);
    }

}
