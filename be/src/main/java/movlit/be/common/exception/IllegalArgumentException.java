package movlit.be.common.exception;

public abstract class IllegalArgumentException extends RuntimeException {

    private final ErrorMessage errorMessage;

    protected IllegalArgumentException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return errorMessage.getCode();
    }

}
