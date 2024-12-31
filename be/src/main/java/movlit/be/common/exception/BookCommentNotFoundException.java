package movlit.be.common.exception;

public class BookCommentNotFoundException extends ResourceNotFoundException {

    public BookCommentNotFoundException() {
        super(ErrorMessage.BOOK_COMMENT_FOUND);
    }


}
