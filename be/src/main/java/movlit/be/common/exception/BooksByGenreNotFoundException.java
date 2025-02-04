package movlit.be.common.exception;

public class BooksByGenreNotFoundException extends ResourceNotFoundException {

    public BooksByGenreNotFoundException() {
        super(ErrorMessage.BOOKS_BY_GENRE_NOT_FOUND);
    }

}
