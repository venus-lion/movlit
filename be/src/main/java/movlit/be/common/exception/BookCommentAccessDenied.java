package movlit.be.common.exception;

import java.nio.file.AccessDeniedException;

public class BookCommentAccessDenied extends AccessDeniedException {

    public BookCommentAccessDenied() {
        super(String.valueOf(ErrorMessage.BOOK_COMMENT_AccessDenied));
    }

}
