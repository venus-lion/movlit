package movlit.be.common.exception;

public class BookBestsellerNotFoundException extends ResourceNotFoundException {

    public BookBestsellerNotFoundException() {
        super(ErrorMessage.BESTSELLERS_NOT_FOUND);
    }

}
