package movlit.be.common.exception;

public class FailedDeserializeException extends ResourceNotFoundException {

    public FailedDeserializeException() {
        super(ErrorMessage.FAILED_DESERIALIZE_DATA);
    }

}
