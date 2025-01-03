package movlit.be.common.exception;

public class MemberAndMovieCommentNotMatchedException extends ResourceNotFoundException {

    public MemberAndMovieCommentNotMatchedException() {
        super(ErrorMessage.MOVIE_NOT_FOUND);
    }

}
