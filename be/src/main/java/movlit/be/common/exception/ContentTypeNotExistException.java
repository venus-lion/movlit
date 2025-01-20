package movlit.be.common.exception;

public class ContentTypeNotExistException extends ResourceNotFoundException {

    public ContentTypeNotExistException() {
        super(ErrorMessage.CONTENT_TYPE_NOT_EXIST);
    }

}
