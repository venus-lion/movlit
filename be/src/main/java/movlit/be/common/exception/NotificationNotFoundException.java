package movlit.be.common.exception;

public class NotificationNotFoundException extends ResourceNotFoundException {

    public NotificationNotFoundException() {
        super(ErrorMessage.NOTIFICATION_NOT_FOUND);
    }

}
